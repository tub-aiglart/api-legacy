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

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Collation
import com.tub_aiglart.api.config.Config
import org.bson.Document

class Database(config: Config) {

    private val client: MongoClient = MongoClients.create(String.format("mongodb://%s:%s@%s:%s", config.get(Config.DB_USERNAME), config.get(Config.DB_PASSWORD), config.get(Config.DB_HOST), config.get(Config.DB_PORT)))
    private val database: MongoDatabase
    val images: MongoCollection<Document>

    init {
        database = client.getDatabase("tub")
        images = database.getCollection("images")
    }
}
