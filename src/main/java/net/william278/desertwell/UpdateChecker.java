package net.william278.desertwell;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * Utility for comparing a {@link Version} against the latest version on SpigotMC
 */
@SuppressWarnings("unused")
public class UpdateChecker {
    // The SpigotMC.org website API endpoint
    private static final String SPIGOT_API_ENDPOINT = "https://api.spigotmc.org/legacy/update.php?resource=";
    @NotNull
    private final Version currentVersion;
    private int resourceId;

    private UpdateChecker() {
        this.currentVersion = new Version();
    }

    private UpdateChecker(@NotNull Version currentVersion, final int resourceId) {
        this.currentVersion = currentVersion;
        this.resourceId = resourceId;
    }

    /**
     * Create a new UpdateChecker for a plugin
     *
     * @param currentVersion The current version of the plugin
     * @param resourceId     The resource ID of the plugin on SpigotMC
     * @return The {@link UpdateChecker}
     */
    public static UpdateChecker create(@NotNull Version currentVersion, final int resourceId) {
        return new UpdateChecker(currentVersion, resourceId);
    }

    /**
     * Return the current plugin {@link Version}
     *
     * @return The current plugin {@link Version}
     */
    @NotNull
    public Version getCurrentVersion() {
        return currentVersion;
    }

    /**
     * Query SpigotMC for the latest {@link Version} of the plugin
     *
     * @return A {@link CompletableFuture} containing the latest {@link Version} of the plugin
     */
    public CompletableFuture<Version> getLatestVersion() {
        return CompletableFuture.supplyAsync(() -> {
            try (final InputStreamReader inputStreamReader = new InputStreamReader(
                    new URL(SPIGOT_API_ENDPOINT + resourceId).openConnection().getInputStream())) {
                return Version.fromString(new BufferedReader(inputStreamReader).readLine());
            } catch (IOException e) {
                throw new IllegalStateException("Unable to fetch latest version", e);
            }
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return new Version();
        });
    }

    /**
     * Check if the current plugin {@link Version} is outdated compared to {@link #getLatestVersion()}
     *
     * @return A {@link CompletableFuture} containing true if the current plugin {@link Version} is outdated
     */
    public CompletableFuture<Boolean> isUpToDate() {
        return getLatestVersion().thenApply(latestVersion -> currentVersion.compareTo(latestVersion) > 0);
    }

}