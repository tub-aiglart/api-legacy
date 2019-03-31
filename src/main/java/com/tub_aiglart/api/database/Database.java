package com.tub_aiglart.api.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.tub_aiglart.api.config.Config;

public class Database {

    private MongoClient client;
    private MongoDatabase database;

    public Database(Config config) {
        this.client = MongoClients.create(String.format("mongodb://%s:%s", config.get("db.host"), config.get("db.port")));
        this.database = client.getDatabase("");
    }
}
