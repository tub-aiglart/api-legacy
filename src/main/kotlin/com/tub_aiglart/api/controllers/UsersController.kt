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
import com.tub_aiglart.api.database.entities.User
import com.tub_aiglart.api.entities.RestError
import com.tub_aiglart.api.enums.Role
import com.tub_aiglart.api.utils.Auth
import com.tub_aiglart.api.utils.badRequest
import com.tub_aiglart.api.utils.unit
import com.tub_aiglart.api.utils.userAccessor
import io.javalin.Context
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import java.util.*

object UsersController {

    fun getUsers(ctx: Context) {
        ctx.json(API.instance.userCache.getAll())
    }

    fun getUser(ctx: Context) {
        val id = ctx.formParam("id") ?: return badRequest(ctx)
        val user = API.instance.userCache[id.toLong()]
        ctx.status(200).json(user)
    }

    fun addUser(ctx: Context) {
        ctx.result("Adding")
    }

    fun editUser(ctx: Context) {
        ctx.result("Editing")
    }

    fun deleteUser(ctx: Context) {
        ctx.result("Deleting")
    }

    fun authorizeUser(ctx: Context) {
        if (ctx.basicAuthCredentials() == null) {
            return ctx.status(400).json(RestError(400, "Bad request", "In order to authorize you need to provide basic credentials!")).unit
        }

        val user = API.instance.userCache.userAccessor().get(ctx.basicAuthCredentials()!!.username).one()

        if (user != null) {
            if (ctx.basicAuthCredentials()!!.password != user.password) {
                ctx.status(401).json(RestError(401, "Unauthorized", "The given password is incorrect!"))
            } else {
                ctx.status(200).json(AuthorizeInfo.fromUser(user, Jwts.builder().signWith(API.instance.key).setId(user.id).setIssuer(user.name).setSubject(user.role.toString()).setIssuedAt(Date()).setExpiration(getDate(7)).compact()))
            }
        } else {
            ctx.status(404).json(RestError(404, "Not found", "A user with the specified username was not found!"))
        }
    }

    fun validateToken(ctx: Context) {
        val token = ctx.header("Authorization") ?: return badRequest(ctx)
        try {
            Jwts.parser().setSigningKey(API.instance.key).parseClaimsJws(token)
            ctx.status(200).json("200")
        } catch (e: JwtException) {
            ctx.status(403).json("403")
        }
    }

    @Suppress("SameParameterValue")
    private fun getDate(days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_WEEK, days)
        return calendar.time
    }

    private data class AuthorizeInfo(
            val id: String,
            val username: String,
            val email: String,
            val token: String
    ) {
        companion object {
            fun fromUser(user: User, token: String): AuthorizeInfo {
                return AuthorizeInfo(
                        user.id,
                        user.name,
                        user.email,
                        token
                )
            }
        }
    }
}
