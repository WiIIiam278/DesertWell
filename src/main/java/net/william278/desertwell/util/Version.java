/*
 * This file is part of DesertWell, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.william278.desertwell.util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * Utility for parsing and then comparing a semantic version string
 */
@SuppressWarnings("unused")
public class Version implements Comparable<Version> {
    public static final String VERSION_DELIMITER = ".";
    public static final String META_DELIMITER = "-";

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
     * @implNote The default meta delimiter that will be used is {@link #META_DELIMITER}
     */
    @NotNull
    public static Version fromString(@NotNull String versionString) {
        return new Version(versionString, META_DELIMITER);
    }

    /**
     * Splits a version string by the version delimiter
     *
     * @param version       The version string to parse
     */
    private String[] splitVersionString(@NotNull String version) {
        return version.split(Pattern.quote(VERSION_DELIMITER));
    }

    /**
     * Parses a version string, including metadata, with the specified delimiter.
     *
     * @param version       The version string to parse
     * @param metaDelimiter The metadata delimiter
     */
    private void parse(@NotNull String version, @NotNull String metaDelimiter) {
        try {
            tryParseDelimitedVersion(version, metaDelimiter);
        } catch (Exception e) {
            tryParsePaperVersion(version);
        }
    }

    /**
     * Tries to parse a standard delimited version: {@code MAJOR.MINOR.PATCH[-METADATA]}.
     * All dot-separated components before the meta delimiter must be valid integers.
     */
    private void tryParseDelimitedVersion(@NotNull String version, @NotNull String metaDelimiter) {
        final int metaIndex = version.indexOf(metaDelimiter);
        final String versionPart;
        final String metadataPart;
        if (metaIndex >= 0) {
            versionPart = version.substring(0, metaIndex);
            metadataPart = version.substring(metaIndex + 1);
        } else {
            versionPart = version;
            metadataPart = "";
        }
        final String[] segments = splitVersionString(versionPart);
        this.versions = Arrays.stream(segments).mapToInt(Integer::parseInt).toArray();
        this.metadata = metadataPart;
    }

    /**
     * Tries to parse a Paper-style version: {@code MAJOR.MINOR[.PATCH].build.NNN-STATUS}.
     * Greedily collects leading integer segments; everything from the first non-integer
     * segment onward is treated as metadata.
     */
    private void tryParsePaperVersion(@NotNull String version) {
        final String[] segments = splitVersionString(version);
        final int[] ints = new int[segments.length];
        int count = 0;
        for (String s : segments) {
            final int val;
            try {
                val = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                break;
            }
            ints[count++] = val;
        }

        // Build the version integer array
        this.versions = Arrays.copyOf(ints, count);

        // Everything after the last integer, joined by ".", is metadata
        if (count < segments.length) {
            final StringJoiner meta = new StringJoiner(VERSION_DELIMITER);
            for (int i = count; i < segments.length; i++) {
                meta.add(segments[i]);
            }
            this.metadata = meta.toString();
        } else {
            this.metadata = "";
        }
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
        return this.versions.length >= 3 ? this.versions[2] : 0;
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
