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
import com.tub_aiglart.api.database.entities.CacheableDatabaseEntity
import com.tub_aiglart.api.database.entities.Image
import com.tub_aiglart.api.database.entities.Snowflake
import com.tub_aiglart.api.database.entities.User
import com.tub_aiglart.api.database.entities.rest.RestSnowflake
import com.tub_aiglart.api.entities.RestError
import com.tub_aiglart.api.utils.Auth
import com.tub_aiglart.api.utils.Generator
import com.tub_aiglart.api.utils.Logger
import com.tub_aiglart.api.utils.Role
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.security.SecurityUtil.roles
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.apache.commons.cli.CommandLine
import java.security.Key

class API(args: CommandLine) {

    companion object {
        @JvmStatic
        lateinit var instance: API
    }

    private val log = Logger.getLogger()

    val config: Config = Config("config/config.yaml")
    private val database: Database
    val app: Javalin

    val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    val generator = Generator(1, 1)

    val userCache: DatabaseCache<User>
    val imageCache: DatabaseCache<Image>

    init {
        instance = this

        database = Database(this.config, this.config[Config.DB_KEYSPACE])
        database.codecRegistry.register(EnumNameCodec(Role::class.java))

        JavalinJackson.getObjectMapper().addMixIn(Snowflake::class.java, RestSnowflake::class.java)
        JavalinJackson.getObjectMapper().addMixIn(CacheableDatabaseEntity::class.java, CacheableDatabaseEntity::class.java)

        userCache = DatabaseCache(User::class, User.Accessor::class.java)
        imageCache = DatabaseCache(Image::class, Image.Accessor::class.java)

        app = Javalin.create().apply {
            accessManager { handler, ctx, permittedRoles ->
                run {
                    when {
                        permittedRoles.contains(Role.EVERYONE) -> handler.handle(ctx)
                        Auth.validateToken(ctx, permittedRoles) -> handler.handle(ctx)
                        else -> ctx.status(403).json(RestError(403, "Forbidden", String.format("In order to interact with '%s' you need at least one of these roles: %s", ctx.path(), permittedRoles.joinToString(", ").toLowerCase())))
                    }
                }
            }
        }

        app.enableCorsForAllOrigins()

        app.start(this.config[Config.REST_PORT])

        app.routes {
            path("image") {
                get(ImagesController::getImages, roles(Role.EVERYONE))
                post(ImagesController::addImage, roles(Role.ADMIN, Role.MANAGER))
                patch(ImagesController::editImage, roles(Role.ADMIN, Role.MANAGER))
                delete(ImagesController::deleteImage, roles(Role.ADMIN, Role.MANAGER))
            }

            get("images", ImagesController::getImages, roles(Role.EVERYONE))

            get("authorize", UsersController::authorizeUser, roles(Role.EVERYONE))

            path("user") {
                get(UsersController::getUser, roles(Role.ADMIN))
                post(UsersController::addUser, roles(Role.ADMIN))
                patch(UsersController::editUser, roles(Role.ADMIN))
                delete(UsersController::deleteUser, roles(Role.ADMIN))
            }

            get("users", UsersController::getUsers, roles(Role.ADMIN))
        }
    }
}
