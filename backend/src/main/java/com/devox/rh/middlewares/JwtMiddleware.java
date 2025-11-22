package com.devox.rh.middlewares;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.handler.JWTAuthHandler;

public class JwtMiddleware {
    public static io.vertx.core.Handler<RoutingContext> requireRole(JWTAuth jwt, String role) {
        return ctx -> {
            JWTAuthHandler.create(jwt).handle(ctx);
            ctx.vertx().setTimer(10, id -> {
                if (ctx.response().ended()) return;
                User user = ctx.user();
                if (user == null) {
                    ctx.response().setStatusCode(401).end("Non autorisé");
                } else if (!role.equals(user.principal().getString("role"))) {
                    ctx.response().setStatusCode(403).end("Accès refusé : rôle requis " + role);
                } else {
                    ctx.next();
                }
            });
        };
    }
}
