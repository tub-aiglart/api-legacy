package com.tub_aiglart.api.controllers

import io.javalin.Context

class PingController: Controller {

    override fun handle(ctx: Context) {
        ctx.result("Hello")
    }
}
