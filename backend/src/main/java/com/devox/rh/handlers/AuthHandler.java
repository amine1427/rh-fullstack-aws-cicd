package com.devox.rh.handlers;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class AuthHandler {
    private final MongoClient mongo;
    private final JWTAuth jwt;

    public AuthHandler(MongoClient mongo, JWTAuth jwt) {
        this.mongo = mongo;
        this.jwt = jwt;
    }

    public void register(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        String email = body.getString("email");
        String password = body.getString("password");
        String role = body.getString("role");

        mongo.findOne("users", new JsonObject().put("email", email), null, res -> {
            if (res.succeeded() && res.result() != null) {
                ctx.response().setStatusCode(400).end("Email déjà utilisé");
            } else {
                JsonObject user = new JsonObject()
                        .put("email", email)
                        .put("password", password)
                        .put("role", role);

                mongo.insert("users", user, insert -> {
                    if (insert.succeeded()) {
                        ctx.response().end("Inscription réussie !");
                    } else {
                        ctx.response().setStatusCode(500).end("Erreur serveur");
                    }
                });
            }
        });
    }

    public void login(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        String email = body.getString("email");
        String password = body.getString("password");

        mongo.findOne("users", new JsonObject().put("email", email), null, res -> {
            if (res.succeeded() && res.result() != null) {
                JsonObject user = res.result();
                if (password.equals(user.getString("password"))) {
                    String token = jwt.generateToken(
                            new JsonObject().put("email", email).put("role", user.getString("role")),
                            new JWTOptions().setExpiresInMinutes(60)
                    );
                    ctx.response().putHeader("Content-Type", "application/json")
                            .end(new JsonObject().put("token", token).encode());
                } else {
                    ctx.response().setStatusCode(401).end("Mot de passe incorrect");
                }
            } else {
                ctx.response().setStatusCode(401).end("Email inconnu");
            }
        });
    }
}
