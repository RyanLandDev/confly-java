package net.ryanland.confly;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConfigFetcher {

    private static OkHttpClient client = new OkHttpClient();

    protected static JsonObject getConfig(String instanceToken) {
        try {
            return fetchConfig(instanceToken).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    protected static CompletableFuture<JsonObject> fetchConfig(String instanceToken) {
        Request request = new Request.Builder()
            .addHeader("Authorization", "Bearer " + instanceToken)
            .url("https://confly.dev/api/config.json")
            .build();

        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                try {
                    // not successful
                    if (!json.get("success").getAsBoolean()) {
                        // 404 Invalid Token
                        if (json.get("status").getAsInt() == 404 && json.get("message").getAsString().equals("Invalid Token")) {
                            throw new AuthenticationException("404 Invalid Token: Invalid instance token provided");
                        }
                    }
                } catch (NullPointerException ignored) {}
                future.complete(json);
            }
        });
        return future;
    }
}
