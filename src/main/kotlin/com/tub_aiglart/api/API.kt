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

package com.tub_aiglart.api

import com.tub_aiglart.api.config.Config
import com.tub_aiglart.api.controllers.AddImageController
import com.tub_aiglart.api.controllers.EditImageController
import com.tub_aiglart.api.controllers.ImagesController
import com.tub_aiglart.api.controllers.RemoveImageController
import com.tub_aiglart.api.database.Database
import com.tub_aiglart.api.utils.Logger
import io.javalin.Javalin
import org.apache.commons.cli.CommandLine

class API(args: CommandLine) {

    private val log = Logger.getLogger()

    private val config: Config = Config("config/config.yaml")
    val database: Database
    private val app: Javalin

    private val imagesController = ImagesController(this)
    private val addImageController = AddImageController(this)
    private val editImageController = EditImageController(this)
    private val removeImageController = RemoveImageController(this)

    init {
        this.database = Database(this.config)
        this.app = Javalin.create().start(this.config.get(Config.REST_PORT))
                .get("images") { imagesController.handle(it) }
                .post("images") { addImageController.handle(it) }
                .patch("images/:id") { editImageController.handle(it) }
                .delete("images/:id") { removeImageController.handle(it) }
    }
}
