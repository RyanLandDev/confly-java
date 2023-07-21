package net.ryanland.confly;

import com.google.gson.JsonObject;

import java.util.concurrent.CompletableFuture;

/**
 * Main class for interacting with Confly.
 * @author RyanDev
 */
public class Confly {

    /**
     * Retrieves the current configuration from Confly using a REST API call. Will halt the current thread until the request has completed.
     * @param instanceToken The token of the instance, found under the 'Instances' tab on the <a href="https://confly.dev/dashboard">Confly Developer Dashboard</a>
     * @return A {@link JsonObject} containing the currently valid config
     * @see #fetchConfig(String)
     */
    public static JsonObject getConfig(String instanceToken) {
        return ConfigFetcher.getConfig(instanceToken);
    }

    /**
     * Retrieves the current configuration from Confly using a REST API call synchronously using a {@link CompletableFuture}.
     * @param instanceToken The token of the instance, found under the 'Instances' tab on the <a href="https://confly.dev/dashboard">Confly Developer Dashboard</a>
     * @return A {@link CompletableFuture<JsonObject>}, issuing either an exception or the currently valid config once the Future resolves
     */
    public static CompletableFuture<JsonObject> fetchConfig(String instanceToken) {
        return ConfigFetcher.fetchConfig(instanceToken);
    }

}
