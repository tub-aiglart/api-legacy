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

import com.datastax.driver.extras.codecs.enums.EnumNameCodec
import com.tub_aiglart.api.config.Config
import com.tub_aiglart.api.controllers.ImagesController
import com.tub_aiglart.api.controllers.UsersController
import com.tub_aiglart.api.database.Database
import com.tub_aiglart.api.database.DatabaseCache
import com.tub_aiglart.api.database.entities.Snowflake
import com.tub_aiglart.api.database.entities.User
import com.tub_aiglart.api.entities.RestError
import com.tub_aiglart.api.utils.Logger
import com.tub_aiglart.api.utils.Role
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.security.SecurityUtil.roles
import org.apache.commons.cli.CommandLine

class API(args: CommandLine) {

    companion object {
        @JvmStatic
        lateinit var instance: API
    }

    private val log = Logger.getLogger()

    val config: Config = Config("config/config.yaml")
    private val database: Database
    private val app: Javalin

    val userCache: DatabaseCache<User>

    init {
        instance = this
        database = Database(this.config, this.config[Config.DB_KEYSPACE])
        database.codecRegistry.register(EnumNameCodec(Role::class.java))
        userCache = DatabaseCache(User::class, User.Accessor::class.java)
        app = Javalin.create().apply {
            accessManager { handler, ctx, permittedRoles ->
                run {
                    println(ctx.header("ID"))
                    when {
                        permittedRoles.contains(Role.EVERYONE) || ctx.header("Authorization") == config[Config.REST_TOKEN] -> handler.handle(ctx)
                        userCache[java.lang.Long.parseUnsignedLong(ctx.header("ID")!!)].role in permittedRoles -> handler.handle(ctx)
                        else -> ctx.status(401).json(RestError(403, "Unauthorized", String.format("In order to interact with '%s' you need at least one of these roles: %s", ctx.path(), permittedRoles.joinToString(", ").toLowerCase())))
                    }
                }
            }
        }.start(this.config[Config.REST_PORT])

        User(xyz.downgoon.snowflake.Snowflake(1,1).nextId(), "Schlaubi", "schl@u.bi", "123", Role.MANAGER).save().thenAccept { println("done") }.exceptionally { it.printStackTrace(); null }

        app.routes {
            path("images") {
                get(ImagesController::getImages, roles(Role.EVERYONE))
                post(ImagesController::addImage, roles(Role.ADMIN, Role.MANAGER))
                patch(ImagesController::editImage, roles(Role.ADMIN, Role.MANAGER))
                delete(ImagesController::deleteImage, roles(Role.ADMIN, Role.MANAGER))
            }

            path("user") {
                get(UsersController::getUser, roles(Role.ADMIN))
                post(UsersController::addUser, roles(Role.ADMIN))
                patch(UsersController::editUser, roles(Role.ADMIN))
                delete(UsersController::deleteUser, roles(Role.ADMIN))
            }

            get("users", UsersController::getUsers, roles(Role.ADMIN))

            get("authorize", UsersController::authorizeUser, roles(Role.EVERYONE))
        }
    }
}
