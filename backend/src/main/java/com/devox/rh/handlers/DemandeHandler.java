package com.devox.rh.handlers;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class DemandeHandler {
    private final MongoClient mongo;

    public DemandeHandler(MongoClient mongo) {
        this.mongo = mongo;
    }

    public void creerDemande(RoutingContext ctx) {
        JsonObject body;
        try {
            body = ctx.body().asJsonObject();
        } catch (Exception e) {
            ctx.response().setStatusCode(400).end("❌ Corps de requête invalide");
            return;
        }

        // DEBUG : Afficher le body reçu
        System.out.println("=== Nouvelle demande reçue ===");
        System.out.println(body.encodePrettily());

        String email = ctx.user().principal().getString("email");
        String type = body.getString("type");
        String motif = body.getString("motif");
        String dateSouhaitee = body.getString("date");

        if (type == null || motif == null || dateSouhaitee == null) {
            ctx.response().setStatusCode(400).end("❌ Champs manquants");
            return;
        }

        JsonObject demande = new JsonObject()
                .put("email", email)
                .put("titre", type)
                .put("description", motif)
                .put("dateSouhaitee", dateSouhaitee)
                .put("statut", "en attente");

        mongo.insert("demandes", demande, res -> {
            if (res.succeeded()) {
                ctx.response()
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("message", "✅ Demande enregistrée").encode());
            } else {
                ctx.response().setStatusCode(500).end("❌ Erreur serveur");
            }
        });
    }

    public void demanderAttestationTravail(RoutingContext ctx) {
        String email = ctx.user().principal().getString("email");

        JsonObject demande = new JsonObject()
                .put("email", email)
                .put("type", "attestation-travail")
                .put("statut", "en attente");

        mongo.insert("demandes", demande, res -> {
            if (res.succeeded()) {
                ctx.response()
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("message", "✅ Demande d’attestation de travail envoyée").encode());
            } else {
                ctx.response().setStatusCode(500).end("❌ Erreur serveur");
            }
        });
    }

    public void demanderAttestationSalaire(RoutingContext ctx) {
        String email = ctx.user().principal().getString("email");

        JsonObject demande = new JsonObject()
                .put("email", email)
                .put("type", "attestation-salaire")
                .put("statut", "en attente");

        mongo.insert("demandes", demande, res -> {
            if (res.succeeded()) {
                ctx.response()
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("message", "✅ Demande d’attestation de salaire envoyée").encode());
            } else {
                ctx.response().setStatusCode(500).end("❌ Erreur serveur");
            }
        });
    }
}
