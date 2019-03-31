package com.tub_aiglart.api;

import com.tub_aiglart.api.config.Config;
import com.tub_aiglart.api.controllers.PingController;
import com.tub_aiglart.api.database.Database;
import io.javalin.Javalin;

public class API {

    private Config config;
    private Database database;
    private Javalin app;

    private API() {
        this.config = new Config("config/config.yaml");
        this.database = new Database(this.config);
        this.app = Javalin.create().start(this.config.getInt("rest.port"))
                .get("/hello", ctx -> new PingController().handle(ctx));

        this.database
    }

    public static void main(String[] args) {
        new API();
    }
}
