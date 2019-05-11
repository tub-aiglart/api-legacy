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

package com.tub_aiglart.api.controllers

import com.tub_aiglart.api.API
import com.tub_aiglart.api.config.Config
import com.tub_aiglart.api.database.entities.Image
import com.tub_aiglart.api.utils.badRequest
import io.javalin.Context
import java.nio.file.Files
import java.nio.file.Paths

object ImagesController {

    fun getImages(ctx: Context) {
        ctx.status(200).json(API.instance.imageCache.getAll())
    }

    fun getImage(ctx: Context) {
        val id = ctx.formParam("id") ?: badRequest(ctx).run { return }
        val image = API.instance.imageCache[id.toLong()]
        ctx.status(200).json(image)
    }

    fun addImage(ctx: Context) {
        val file = ctx.uploadedFiles("files").first()

        val title = ctx.formParam("title") ?: return badRequest(ctx)
        val description = ctx.formParam("description") ?: return badRequest(ctx)
        val size = ctx.formParam("size") ?: return badRequest(ctx)
        val displayed = ctx.formParam("displayed", Boolean::class.java).get()

        val entry =  API.instance.imageCache[API.instance.generator.nextId()]
        entry.title = title
        entry.description = description
        entry.size = size
        entry.isDisplayed = displayed
        entry.extension = file.extension
        val path = API.instance.config.get<String>(Config.CDN_PATH)
        val out = Paths.get("$path${entry.id}${file.extension}")

        val parent = out.parent.toFile()
        if (!parent.exists()) {
            parent.mkdirs()
        }

        file.content.use {
            Files.copy(it, out)
        }

        entry.save()
        ctx.status(201).json(entry)
    }

    fun editImage(ctx: Context) {
        val id = ctx.formParam("id")!!.toLongOrNull() ?: return badRequest(ctx)
        val title = ctx.formParam("title") ?: return badRequest(ctx)
        val description = ctx.formParam("description") ?: return badRequest(ctx)
        val size = ctx.formParam("size") ?: return badRequest(ctx)
        val displayed = ctx.formParam("displayed", Boolean::class.java).get()

        val image = API.instance.imageCache[id]
        image.title = title
        image.description = description
        image.size = size
        image.isDisplayed = displayed
        image.save()

        ctx.status(201).json(image)
    }

    fun deleteImage(ctx: Context) {
        ctx.result("Deleting")
    }
}
