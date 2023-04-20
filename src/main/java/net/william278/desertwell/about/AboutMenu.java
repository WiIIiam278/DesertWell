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

package net.william278.desertwell.about;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.william278.desertwell.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Utility for displaying a menu of information about a plugin
 */
@SuppressWarnings("unused")
public class AboutMenu {
    private final Component title;
    private final Component description;
    private final Version version;
    private final TextColor themeColor;
    private final TextColor secondaryColor;
    private final Map<String, List<Credit>> attributions;
    private final List<Link> buttons;

    private AboutMenu(@NotNull Component title, @Nullable Component description, @Nullable Version version,
                      @NotNull TextColor themeColor, @NotNull TextColor secondaryColor,
                      @NotNull Map<String, List<Credit>> attributions, @NotNull List<Link> buttons) {
        this.title = title;
        this.description = description;
        this.version = version;
        this.themeColor = themeColor;
        this.secondaryColor = secondaryColor;
        this.attributions = attributions;
        this.buttons = buttons;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public TextComponent toComponent() {
        final TextComponent.Builder builder = Component.text()
                .append(Component.newline())
                .append(title.colorIfAbsent(themeColor).decorate(TextDecoration.BOLD));
        if (version != null) {
            builder.append(Component.text(" | v" + version).color(themeColor));
        }

        // Add description
        if (description != null) {
            builder.append(Component.newline()).append(description.colorIfAbsent(secondaryColor));
        }

        // Add attributions
        if (!attributions.isEmpty()) {
            builder.append(Component.newline());
        }
        for (Map.Entry<String, List<Credit>> entry : attributions.entrySet()) {
            builder.append(Component.newline())
                    .append(Component.text("• " + entry.getKey() + ": ").color(NamedTextColor.WHITE));
            entry.getValue().stream().map(Credit::toComponent).forEach(name -> builder
                    .append(name)
                    .append(Component.text(", ")));
        }

        // Add buttons
        if (!buttons.isEmpty()) {
            builder.append(Component.newline()).append(Component.newline())
                    .append(Component.text("Links: ").color(secondaryColor));
            buttons.stream().map(Link::toComponent).forEach(link -> builder
                    .append(link)
                    .append(Component.text("   ")));
        }

        return builder.build();
    }

    public static class Builder {
        private Component title;
        private Component description;
        private Version version;
        private TextColor themeColor = TextColor.color(0x00FB9A);
        private TextColor secondaryColor = TextColor.color(0xAAAAAA);
        private final Map<String, List<Credit>> attributions = new LinkedHashMap<>();
        private final List<Link> buttons = new ArrayList<>();

        private Builder() {
        }

        /**
         * Set the title of the resource to display on the menu
         *
         * @param title The resource title
         * @return The {@link Builder}
         */
        @NotNull
        public Builder title(@NotNull Component title) {
            this.title = title;
            return this;
        }

        /**
         * Set the description of the resource to display on the menu
         *
         * @param description The resource description
         * @return The {@link Builder}
         */
        @NotNull
        public Builder description(@Nullable Component description) {
            this.description = description;
            return this;
        }

        /**
         * Set the theme color of the resource to display on the menu
         *
         * @param themeColor The resource theme color
         * @return The {@link Builder}
         */
        @NotNull
        public Builder themeColor(@NotNull TextColor themeColor) {
            this.themeColor = themeColor;
            return this;
        }

        /**
         * Set the secondary color of the resource to display on the menu
         *
         * @param secondaryColor The resource secondary color
         * @return The {@link Builder}
         */
        @NotNull
        public Builder secondaryColor(@NotNull TextColor secondaryColor) {
            this.secondaryColor = secondaryColor;
            return this;
        }

        /**
         * Set the {@link Version} of the resource to display on the menu
         *
         * @param version The resource version
         * @return The {@link Builder}
         */
        @NotNull
        public Builder version(@NotNull Version version) {
            this.version = version;
            return this;
        }

        /**
         * Add an attribution to the menu
         *
         * @param category The attribution category (e.g. {@code "Author"})
         * @param credits  {@link Credit}s to add
         * @return The {@link Builder}
         */
        @NotNull
        public Builder credits(@NotNull String category, @NotNull Credit... credits) {
            final List<Credit> creditList = new ArrayList<>(Arrays.asList(credits));
            attributions.putIfAbsent(category, new ArrayList<>());
            attributions.get(category).addAll(creditList);
            return this;
        }

        /**
         * Add linked buttons to the menu
         *
         * @param links {@link Link}s to add
         * @return The {@link Builder}
         */
        @NotNull
        public Builder buttons(@NotNull Link... links) {
            buttons.addAll(Arrays.asList(links));
            return this;
        }

        /**
         * Build the {@link AboutMenu}
         *
         * @return The {@link Builder}
         */
        @NotNull
        public AboutMenu build() {
            if (title == null) {
                throw new IllegalStateException("Title must be set");
            }
            return new AboutMenu(title, description, version, themeColor, secondaryColor, attributions, buttons);
        }

    }

    /**
     * Represents a link related to the resource
     */
    public static class Link {
        private String text = "Link";
        private TextColor color = TextColor.color(0x00fb9a);
        private final String url;
        private String icon;

        private Link(@NotNull String url) {
            this.url = url;
        }

        /**
         * Create a link from a URL
         *
         * @param url The URL
         * @return The {@link Link}
         */
        public static Link of(@NotNull String url) {
            return new Link(url);
        }

        /**
         * Set the text describing the link
         *
         * @param text The text
         * @return The {@link Link}
         */
        public Link text(@NotNull String text) {
            this.text = text;
            return this;
        }

        /**
         * Set the link button icon. Note this will not affect the string representation of the menu
         *
         * @param icon The icon character
         * @return The {@link Link}
         */
        public Link icon(@NotNull String icon) {
            this.icon = icon;
            return this;
        }

        /**
         * Set the link button color. Note this will not affect the string representation of the menu
         *
         * @param color The color
         * @return The {@link Link}
         */
        public Link color(@NotNull TextColor color) {
            this.color = color;
            return this;
        }

        @NotNull
        public Component toComponent() {
            return Component.text("[" + (icon == null ? "" : icon) + " " + text + "]", color)
                    .clickEvent(ClickEvent.openUrl(url));
        }

    }

    /**
     * Represents information about someone who worked on the resource
     */
    public static class Credit {
        private final String name;
        @Nullable
        private String description;
        @Nullable
        private String url;
        private TextColor color = TextColor.color(0xAAAAAA);

        private Credit(@NotNull String name) {
            this.name = name;
        }

        /**
         * Create a credit from someone's name
         *
         * @param name The name of the person to credit
         * @return The {@link Credit}
         */
        @NotNull
        public static Credit of(@NotNull String name) {
            return new Credit(name);
        }

        /**
         * Set the description of the credit; i.e. what they did
         *
         * @param description The description of the credit (what they did)
         * @return The {@link Credit}
         */
        @NotNull
        public Credit description(@Nullable String description) {
            this.description = description;
            return this;
        }

        /**
         * Set the URL of the credit; i.e. their website. Note this has no effect on the string representation of the menu
         *
         * @param url The URL of the credit (i.e. their website)
         * @return The {@link Credit}
         */
        @NotNull
        public Credit url(@Nullable String url) {
            this.url = url;
            return this;
        }

        /**
         * Set the color of the credit. Note this has no effect on the string representation of the menu
         *
         * @param color The color of the credit
         * @return The {@link Credit}
         */
        @NotNull
        public Credit color(@NotNull TextColor color) {
            this.color = color;
            return this;
        }

        @NotNull
        public Component toComponent() {
            final ComponentBuilder<TextComponent, TextComponent.Builder> builder = Component.text().content(name);
            if (description != null) {
                builder.append(Component.text(" (" + description + ")"));
            }
            if (url != null) {
                builder.clickEvent(ClickEvent.openUrl(url));
            }
            return builder.color(color).build();
        }

    }

}
