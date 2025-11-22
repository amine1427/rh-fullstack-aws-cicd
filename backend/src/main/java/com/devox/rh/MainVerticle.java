package com.devox.rh;

import com.devox.rh.handlers.AuthHandler;
import com.devox.rh.handlers.DemandeHandler;
import com.devox.rh.middlewares.JwtMiddleware;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) {
        ConfigRetriever retriever = ConfigRetriever.create(vertx,
                new ConfigRetrieverOptions().addStore(
                        new ConfigStoreOptions()
                                .setType("file")
                                .setFormat("json")
                                .setConfig(new JsonObject().put("path", "config.json"))
                )
        );

        retriever.getConfig().onSuccess(config -> {

            // ---------- MongoDB : override avec variables d'environnement si présentes ----------
            String mongoUriEnv = System.getenv("MONGO_URI");
            String mongoDbEnv = System.getenv("MONGO_DB");

            if (mongoUriEnv != null && !mongoUriEnv.isEmpty()) {
                config.put("connection_string", mongoUriEnv);
            }
            if (mongoDbEnv != null && !mongoDbEnv.isEmpty()) {
                config.put("db_name", mongoDbEnv);
            }

            MongoClient mongo = MongoClient.createShared(vertx, config);

            // ---------- JWT : secret configurable ----------
            String jwtSecret = System.getenv("JWT_SECRET");
            if (jwtSecret == null || jwtSecret.isEmpty()) {
                // valeur par défaut pour le dev local
                jwtSecret = "my-super-secret-key";
            }

            JWTAuth jwt = JWTAuth.create(vertx, new JWTAuthOptions()
                    .addPubSecKey(new PubSecKeyOptions()
                            .setAlgorithm("HS256")
                            .setBuffer(jwtSecret))
            );

            AuthHandler authHandler = new AuthHandler(mongo, jwt);
            DemandeHandler demandeHandler = new DemandeHandler(mongo);

            Router router = Router.router(vertx);

            // ---------- Middleware CORS ----------
            router.route().handler(ctx -> {
                ctx.response()
                        .putHeader("Access-Control-Allow-Origin", "*")
                        .putHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                        .putHeader("Access-Control-Allow-Headers", "*");
                if ("OPTIONS".equalsIgnoreCase(ctx.request().method().name())) {
                    ctx.response().setStatusCode(204).end();
                } else {
                    ctx.next();
                }
            });

            router.route().handler(BodyHandler.create());

            // ---------- Routes API ----------
            router.get("/test").handler(ctx -> ctx.response().end("✅ Serveur opérationnel"));

            router.post("/api/register").handler(authHandler::register);
            router.post("/api/login").handler(authHandler::login);

            // Demandes RH
            router.post("/api/demande")
                    .handler(JwtMiddleware.requireRole(jwt, "employe"))
                    .handler(demandeHandler::creerDemande);

            // Routes d’attestations
            router.post("/api/demande/attestation-travail")
                    .handler(JwtMiddleware.requireRole(jwt, "employe"))
                    .handler(demandeHandler::demanderAttestationTravail);

            router.post("/api/demande/attestation-salaire")
                    .handler(JwtMiddleware.requireRole(jwt, "employe"))
                    .handler(demandeHandler::demanderAttestationSalaire);

            // ---------- Static files pour le frontend (plus tard on mettra le build Vue ici) ----------
            // Les fichiers seront dans src/main/resources/webroot/
            router.get("/*").handler(StaticHandler.create("webroot"));

            // ---------- Port dynamique (AWS) ----------
            final int port;
            String portEnv = System.getenv("PORT");

            if (portEnv != null && !portEnv.isEmpty()) {
                int parsed;
                try {
                    parsed = Integer.parseInt(portEnv);
                } catch (NumberFormatException e) {
                    System.out.println("PORT invalide, utilisation du port par défaut 8888");
                    parsed = 8888;
                }
                port = parsed;
            } else {
                port = 8888; // dev local
            }


            vertx.createHttpServer()
                    .requestHandler(router)
                    .listen(port)
                    .onSuccess(ok -> {
                        System.out.println(" Serveur lancé sur http://localhost:" + port);
                        startPromise.complete();
                    })
                    .onFailure(startPromise::fail);

        }).onFailure(startPromise::fail);
    }
}
