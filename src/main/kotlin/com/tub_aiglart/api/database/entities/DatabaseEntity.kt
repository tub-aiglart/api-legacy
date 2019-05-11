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

package com.tub_aiglart.api.database.entities

import com.datastax.driver.core.ConsistencyLevel
import com.datastax.driver.mapping.Mapper
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.tub_aiglart.api.database.Database
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.Executors

abstract class DatabaseEntity<T> {

    companion object {
        private val MAPPER_POOL = mutableMapOf<Class<*>, Mapper<*>>()
        private val EXECUTOR = Executors.newCachedThreadPool()
    }

    @Transient
    private val mapper = mapper()

    @Suppress("UNCHECKED_CAST")
    open fun delete(): CompletionStage<Void> {
        return operate(mapper.deleteAsync(this as T))
    }

    @Suppress("UNCHECKED_CAST")
    open fun save(): CompletionStage<Void> {
        return operate(mapper.saveAsync(this as T))
    }

    @Suppress("UnstableApiUsage")
    private fun operate(listenableFuture: ListenableFuture<Void>): CompletionStage<Void> {
        val future = CompletableFuture<Void>()
        Futures.addCallback(listenableFuture, object : FutureCallback<Void> {
            override fun onSuccess(result: Void?) {
                future.complete(result)
            }

            override fun onFailure(t: Throwable) {
                future.completeExceptionally(t)
            }

        }, EXECUTOR)
        return future
    }

    @Suppress("UNCHECKED_CAST")
    private fun mapper(): Mapper<T> {
        return MAPPER_POOL.computeIfAbsent(this::class.java) {
            val mapper = Database.instance.mappingManager.mapper(it)
            mapper.setDefaultGetOptions(Mapper.Option.consistencyLevel(ConsistencyLevel.ALL))
            mapper
        } as Mapper<T>
    }
}
