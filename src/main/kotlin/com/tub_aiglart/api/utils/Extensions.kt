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

package com.tub_aiglart.api.utils

import com.tub_aiglart.api.database.DatabaseCache
import com.tub_aiglart.api.database.entities.User
import com.tub_aiglart.api.entities.RestError
import io.javalin.Context

fun DatabaseCache<User>.userAccessor(): User.Accessor {
    return this.accessor as User.Accessor
}

val Any?.unit get() = Unit

fun badRequest(ctx: Context) {
    return ctx.status(400).json(RestError(400, "Bad Request", "")).unit
}
