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

import com.tub_aiglart.api.API
import com.tub_aiglart.api.enums.Role
import io.javalin.Context
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts

class Auth {

    companion object {

        fun validateToken(ctx: Context, permittedRoles: Set<io.javalin.security.Role>): Boolean {
            val token = ctx.header("Authorization") ?: return badRequest(ctx).run { false }
            return try {
                Role.valueOf(Jwts.parser().setSigningKey(API.instance.key).parseClaimsJws(token).body.subject) in permittedRoles
            } catch (e: JwtException) {
                false
            }
        }
    }
}
