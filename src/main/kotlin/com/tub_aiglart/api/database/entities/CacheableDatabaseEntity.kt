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

import com.datastax.driver.mapping.annotations.Transient
import com.fasterxml.jackson.annotation.JsonIgnore
import com.tub_aiglart.api.database.DatabaseCache
import java.util.concurrent.CompletionStage

abstract class CacheableDatabaseEntity<T : CacheableDatabaseEntity<T>>(id: Long = -1) : SnowflakeDatabaseEntity<T>(id) {

    @Suppress("ProtectedInFinal")
    @Transient
    @JsonIgnore
    open lateinit var cache: DatabaseCache<T>

    @Suppress("UNCHECKED_CAST")
    override fun save(): CompletionStage<Void> {
        if (this::cache.isInitialized) {
            cache.update(this as T)
        }
        return super.save()
    }

    @Suppress("UNCHECKED_CAST")
    override fun delete(): CompletionStage<Void> {
        if (this::cache.isInitialized) {
            cache.delete(this as T)
        }
        return super.delete()
    }
}
