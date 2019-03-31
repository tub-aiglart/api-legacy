package com.tub_aiglart.api.controllers

import io.javalin.Context

interface Controller {
    fun handle(ctx: Context)
}
