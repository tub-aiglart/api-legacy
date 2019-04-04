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
import io.javalin.Context

object ImagesController {

    private val api = API.instance

    fun getImages(ctx: Context) {
        ctx.result(String.format("test %s", api.config[Config.DB_HOST]))
    }

    fun addImage(ctx: Context) {
        ctx.result("Adding")
    }

    fun editImage(ctx: Context) {
        ctx.result("Editing")
    }

    fun deleteImage(ctx: Context) {
        ctx.result("Deleting")
    }
}
