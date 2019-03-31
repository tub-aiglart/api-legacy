package com.tub_aiglart.api.config

import org.simpleyaml.configuration.file.YamlFile
import java.nio.file.Files
import java.nio.file.Paths

class Config(path: String): YamlFile(path) {

    init {
        if (!Files.exists(Paths.get(path))) {
            this.createNewFile(true)
            setDefaults()
            this.save()
        } else {
            this.load()
        }
    }

    private fun setDefaults() {
        /* REST API CREDENTIALS */
        set("rest.port", 1337)

        /* DATABASE CREDENTIALS */
        set("db.host", 1337)
        set("db.port", 1337)
        set("db.username", "default")
        set("db.password", "default")
    }
}
