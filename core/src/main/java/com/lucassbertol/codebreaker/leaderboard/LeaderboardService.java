package com.lucassbertol.codebreaker.leaderboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;

public class LeaderboardService {

    public interface SubmitCallback {
        void onSuccess();
        void onError(String message);
    }

    public LeaderboardService() {
    }

    public void submitScore(String nome, int pontos, SubmitCallback callback) {
        if (nome == null || nome.trim().isEmpty()) {
            if (callback != null) {
                callback.onError("Nome inválido");
            }
            return;
        }

        // Monta JSON manualmente: {"nome":"...","score":123}
        String jsonBody = "{\"nome\":\"" + escapeJson(nome) + "\",\"score\":" + pontos + "}";

        String url = LeaderboardConfig.getAppendUrl();

        Gdx.app.log("LeaderboardService", "JSON enviado: " + jsonBody);
        Gdx.app.log("LeaderboardService", "Enviando score para: " + url);

        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
        request.setUrl(url);
        request.setHeader("Content-Type", "application/json; charset=utf-8");
        request.setContent(jsonBody);

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                int statusCode = status.getStatusCode();
                String result = httpResponse.getResultAsString();

                Gdx.app.log("LeaderboardService",
                    "Resposta HTTP " + statusCode + ": " + result);

                if (statusCode >= 200 && statusCode < 300) {
                    if (callback != null) {
                        Gdx.app.postRunnable(callback::onSuccess);
                    }
                } else {
                    if (callback != null) {
                        Gdx.app.postRunnable(() ->
                            callback.onError("Erro HTTP: " + statusCode));
                    }
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("LeaderboardService",
                    "Falha ao enviar score: " + t.getMessage(), t);
                if (callback != null) {
                    Gdx.app.postRunnable(() ->
                        callback.onError("Falha na requisição: " + t.getMessage()));
                }
            }

            @Override
            public void cancelled() {
                Gdx.app.log("LeaderboardService", "Requisição cancelada");
                if (callback != null) {
                    Gdx.app.postRunnable(() ->
                        callback.onError("Requisição cancelada"));
                }
            }
        });
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"");
    }
}
