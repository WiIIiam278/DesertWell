package net.william278.desertwell;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * Utility for parsing and then comparing a semantic version string
 */
@SuppressWarnings("unused")
public class Version implements Comparable<Version> {
    private final String VERSION_DELIMITER = ".";
    private static final String MINECRAFT_META_DELIMITER = "-";
    private static final String PLUGIN_META_DELIMITER = "+";
    // Major, minor and patch version numbers
    private int[] versions = new int[]{};
    @NotNull
    private String metadata = "";
    @NotNull
    private String metaSeparator = "";

    protected Version() {
    }

    private Version(@NotNull String version, @NotNull String metaDelimiter) {
        this.parse(version, metaDelimiter);
        this.metaSeparator = metaDelimiter;
    }

    /**
     * Create a new {@link Version} by parsing a string
     *
     * @param version       The version string to parse
     * @param metaDelimiter The delimiter separating version numbers from metadata to use
     * @return The {@link Version}
     */
    @NotNull
    public static Version fromString(@NotNull String version, @NotNull String metaDelimiter) {
        return new Version(version, metaDelimiter);
    }

    /**
     * Create a new {@link Version} by parsing a string
     *
     * @param versionString The version string to parse
     * @return The {@link Version}
     * @implNote The default meta delimiter that will be used is {@link #PLUGIN_META_DELIMITER}
     */
    @NotNull
    public static Version fromString(@NotNull String versionString) {
        return new Version(versionString, PLUGIN_META_DELIMITER);
    }

    /**
     * Create a new {@link Version} by parsing a Minecraft string
     *
     * @param versionString The Minecraft version string to parse
     * @return The {@link Version}
     * @implNote The  meta delimiter that will be used is {@link #MINECRAFT_META_DELIMITER}
     */
    @NotNull
    public static Version fromMinecraftVersionString(@NotNull String versionString) {
        return new Version(versionString, MINECRAFT_META_DELIMITER);
    }

    /**
     * Parses a version string, including metadata, with the specified delimiter
     *
     * @param version       The version string to parse
     * @param metaDelimiter The metadata delimiter
     */
    private void parse(@NotNull String version, @NotNull String metaDelimiter) {
        int metaIndex = version.indexOf(metaDelimiter);
        if (metaIndex > 0) {
            this.metadata = version.substring(metaIndex + 1);
            version = version.substring(0, metaIndex);
        }
        String[] versions = version.split(Pattern.quote(VERSION_DELIMITER));
        this.versions = Arrays.stream(versions).mapToInt(Integer::parseInt).toArray();
    }

    /**
     * Compare this {@link Version} to another {@link Version}
     *
     * @param other the object to be compared.
     * @return a negative integer, zero, or a positive integer as this version is less than, equal to, or greater
     * than the other version in terms of the semantic major, minor and patch versioning standard.
     */
    @Override
    public int compareTo(@NotNull Version other) {
        int length = Math.max(this.versions.length, other.versions.length);
        for (int i = 0; i < length; i++) {
            int a = i < this.versions.length ? this.versions[i] : 0;
            int b = i < other.versions.length ? other.versions[i] : 0;

            if (a < b) return -1;
            if (a > b) return 1;
        }

        return 0;
    }

    /**
     * Get the string representation of this {@link Version}
     *
     * @return The string representation of this {@link Version}
     */
    @Override
    @NotNull
    public String toString() {
        final StringJoiner joiner = new StringJoiner(VERSION_DELIMITER);
        for (int version : this.versions) {
            joiner.add(String.valueOf(version));
        }
        return joiner + ((!this.metadata.isEmpty()) ? (this.metaSeparator + this.metadata) : "");
    }

    /**
     * Get the string representation of this {@link Version}, without metadata
     *
     * @return The string representation of this {@link Version} without metadata
     */
    @NotNull
    public String toStringWithoutMetadata() {
        final StringJoiner joiner = new StringJoiner(VERSION_DELIMITER);
        for (int version : this.versions) {
            joiner.add(String.valueOf(version));
        }
        return joiner.toString();
    }

    /**
     * Get the major version number.
     *
     * @return The major version number.
     */
    public int getMajor() {
        return this.versions[0];
    }

    /**
     * Get the minor version number.
     *
     * @return The minor version number.
     */
    public int getMinor() {
        return this.versions[1];
    }

    /**
     * Get the patch version number.
     *
     * @return The patch version number.
     */
    public int getPatch() {
        return this.versions[2];
    }

    /**
     * Get the version metadata.
     *
     * @return The metadata.
     */
    @NotNull
    public String getMetadata() {
        return this.metadata;
    }

}
