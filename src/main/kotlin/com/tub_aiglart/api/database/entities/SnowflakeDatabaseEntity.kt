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

import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.fasterxml.jackson.annotation.JsonProperty

abstract class SnowflakeDatabaseEntity<T>(): DatabaseEntity<T>(), Snowflake {

    @PartitionKey
    @Column(name = "id")
    @JsonProperty("id")
    override var idLong: Long = -1

    @Suppress("ConvertSecondaryConstructorToPrimary", "LeakingThis")
    constructor(id: Long): this() {
        this.idLong = id
    }
}