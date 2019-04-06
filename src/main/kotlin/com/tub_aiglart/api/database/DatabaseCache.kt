/*
 * API - A basic REST API made for tub-aiglart.com
 *
 * Copyright (C) 2019  Oskar Lang
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package com.tub_aiglart.api.database

import com.datastax.driver.mapping.Result
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.tub_aiglart.api.database.entities.CacheableDatabaseEntity
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.TimeUnit
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class DatabaseCache<T : CacheableDatabaseEntity<T>>(
        type: KClass<T>,
        accessor: Class<out CacheAccessor<T>>,
        expiry: Long = 10,
        expiryUnit: TimeUnit = TimeUnit.HOURS
) {

    val accessor = Database.instance.mappingManager.createAccessor(accessor)!!
    private val cache: LoadingCache<Long, T> = CacheBuilder.newBuilder()
            .expireAfterWrite(expiry, expiryUnit)
            .build(object : CacheLoader<Long, T>() {
                override fun load(key: Long): T {
                    val result = this@DatabaseCache.accessor.get(key)
                    val entity = if (result.availableWithoutFetching > 0) {
                        result.one()
                    } else {
                        constructor.call(key)
                    }
                    entity.cache = this@DatabaseCache
                    return entity
                }
            })
    private val constructor: KCallable<T>

    init {
        val primaryConstructor = type.findAnnotation<CacheConstructor>()
        constructor = if (primaryConstructor != null) {
            type.primaryConstructor
                    ?: throw IllegalStateException("Class annotation does only support primary constructors")
        } else {
            type.constructors.firstOrNull { it.findAnnotation<CacheConstructor>() != null }
                    ?: throw IllegalStateException("Could not find @CacheConstructor")
        }
    }

    operator fun get(id: Long): T {
        return cache[id]
    }

    fun getAll(): ConcurrentMap<Long, T> {
        return cache.asMap()
    }

    fun update(entity: T) {
        cache.refresh(entity.idLong)
    }

    fun delete(entity: T) {
        cache.invalidate(entity.idLong)
    }
}

interface CacheAccessor<T> {
    fun get(id: Long): Result<T>
}

@Target(AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheConstructor
