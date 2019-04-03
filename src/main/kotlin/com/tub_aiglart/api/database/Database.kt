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

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.CodecRegistry
import com.datastax.driver.core.PlainTextAuthProvider
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.MappingManager
import com.tub_aiglart.api.config.Config

class Database(
        config: Config,
        val keyspace: String
) {

    companion object {
        @JvmStatic
        lateinit var instance: Database
    }

    val cluster: Cluster
    val codecRegistry = CodecRegistry()
    val session: Session
    val mappingManager: MappingManager

    init {
        instance = this
        cluster = Cluster.builder()
                .addContactPoints(config.get<String>(Config.DB_HOST))
                .withAuthProvider(PlainTextAuthProvider(config.get(Config.DB_USERNAME), config.get(Config.DB_PASSWORD)))
                .withCodecRegistry(codecRegistry)
                .build()

        session = cluster.connect(keyspace)
        mappingManager = MappingManager(session)
    }
}
