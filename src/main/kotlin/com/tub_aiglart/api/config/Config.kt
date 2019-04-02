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

package com.tub_aiglart.api.config

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.core.file.FileConfig

class Config(private val config: FileConfig) : Config {

    companion object {
        const val REST_PORT = "rest.port"
        const val DB_HOST = "db.host"
        const val DB_PORT = "db.port"
        const val DB_USERNAME = "db.username"
        const val DB_PASSWORD = "db.password"
    }

    constructor(path: String) : this(FileConfig.of(path))

    init {
        val file = config.file
        if (!file.exists()) {
            val parent = file.parentFile
            if (!parent.exists()) {
                parent.mkdirs()
            }
        }
        config.load()
        setDefaults()
        config.save()
    }

    private fun setDefault(key: String, value: Any) {
        if (!super.contains(key)) {
            super.add(key, value)
        }
    }

    private fun setDefaults() {
        setDefault(REST_PORT, 1337)
        setDefault(DB_HOST, 1337)
        setDefault(DB_PORT, 1337)
        setDefault(DB_USERNAME, "default")
        setDefault(DB_PASSWORD, "default")
    }

    override fun clear() {
        config.clear()
    }

    override fun <T : Any?> getRaw(path: MutableList<String>?): T {
        return config.getRaw(path)
    }

    override fun createSubConfig(): Config {
        return config.createSubConfig()
    }

    override fun add(path: MutableList<String>?, value: Any?): Boolean {
        return config.add(path, value)
    }

    override fun size(): Int {
        return config.size()
    }

    override fun entrySet(): MutableSet<out Config.Entry> {
        return config.entrySet()
    }

    override fun <T : Any?> remove(path: MutableList<String>?): T {
        return config.remove(path)
    }

    override fun <T : Any?> set(path: MutableList<String>?, value: Any?): T {
        return config.set(path, value)
    }

    override fun valueMap(): MutableMap<String, Any> {
        return config.valueMap()
    }

    override fun configFormat(): ConfigFormat<*> {
        return config.configFormat()
    }

    override fun contains(path: MutableList<String>?): Boolean {
        return config.contains(path)
    }
}
