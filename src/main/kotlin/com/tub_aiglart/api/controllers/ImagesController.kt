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
        ctx.json(API.instance.imageCache.getAll())
    }

    fun addImage(ctx: Context) {
        val file = ctx.uploadedFiles("files").first()
        val title = ctx.formParam("title") ?: return badRequest(ctx)
        val description = ctx.formParam("description") ?: return badRequest(ctx)
        val size = ctx.formParam("size") ?: return badRequest(ctx)
        val displayed = ctx.formParam("displayed", Boolean::class.java).get()
        val entry = Image(API.instance.generator.nextId(), title, description, size, displayed)
        val out = Paths.get("C:\\Users\\oskar\\Desktop\\uploads\\${entry.id}_${entry.title}${file.extension}")
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
        ctx.result("Editing")
    }

    fun deleteImage(ctx: Context) {
        ctx.result("Deleting")
    }
}
