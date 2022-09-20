package net.william278.desertwell;

import de.themoep.minedown.adventure.MineDown;
import de.themoep.minedown.adventure.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Utility for displaying a menu of information about a plugin
 */
@SuppressWarnings("unused")
public class AboutMenu {
    @NotNull
    private final String title;
    @Nullable
    private Version version;
    @Nullable
    private String description;
    @NotNull
    private final Map<String, List<Credit>> attributions;
    @NotNull
    private final List<Link> buttons;

    private AboutMenu() {
        this.title = "";
        this.buttons = new ArrayList<>();
        this.attributions = new LinkedHashMap<>();
    }

    private AboutMenu(@NotNull String title) {
        this.title = title;
        this.buttons = new ArrayList<>();
        this.attributions = new LinkedHashMap<>();
    }

    /**
     * Create an about menu
     *
     * @param title The title of the menu (resource title)
     * @return The {@link AboutMenu}
     */
    @NotNull
    public static AboutMenu create(@NotNull String title) {
        return new AboutMenu(title);
    }

    /**
     * Set the description of the resource to display on the menu
     *
     * @param description The resource description
     * @return The {@link AboutMenu}
     */
    @NotNull
    public AboutMenu withDescription(@Nullable String description) {
        this.description = description;
        return this;
    }

    /**
     * Set the {@link Version} of the resource to display on the menu
     *
     * @param version The resource version
     * @return The {@link AboutMenu}
     */
    @NotNull
    public AboutMenu withVersion(@NotNull Version version) {
        this.version = version;
        return this;
    }

    /**
     * Add an attribution to the menu
     *
     * @param category The attribution category (e.g. {@code "Author"})
     * @param credits  {@link Credit}s to add
     * @return The {@link AboutMenu}
     */
    @NotNull
    public AboutMenu addAttribution(@NotNull String category, @NotNull Credit... credits) {
        final List<Credit> creditList = new ArrayList<>(Arrays.asList(credits));
        attributions.putIfAbsent(category, new ArrayList<>());
        attributions.get(category).addAll(creditList);
        return this;
    }

    /**
     * Add linked buttons to the menu
     *
     * @param links {@link Link}s to add
     * @return The {@link AboutMenu}
     */
    @NotNull
    public AboutMenu addButtons(@NotNull Link... links) {
        buttons.addAll(Arrays.asList(links));
        return this;
    }

    /**
     * Return the formatted {@link MineDown} menu
     *
     * @return The {@link MineDown} menu
     */
    @NotNull
    public MineDown toMineDown() {
        final StringJoiner menu = new StringJoiner("\n")
                .add("[" + escapeMineDown(title) + "](#00fb9a bold)"
                     + (version != null ? " [| v" + escapeMineDown(version.toString()) + "](#00fb9a)" : ""));
        if (description != null) {
            menu.add("[" + escapeMineDown(description) + "](gray)");
        }

        if (!attributions.isEmpty()) {
            menu.add("");
        }
        for (Map.Entry<String, List<Credit>> entry : attributions.entrySet()) {
            final StringJoiner creditJoiner = new StringJoiner(", ");
            for (final Credit credit : entry.getValue()) {
                creditJoiner.add("[" + credit.name + "](" + credit.color +
                                 (credit.description != null ? " show_text=&7" + escapeMineDown(credit.description) : "") +
                                 (credit.url != null ? " open_url=" + escapeMineDown(credit.url) : "")
                                 + ")");
            }

            if (!creditJoiner.toString().isBlank()) {
                menu.add("[• " + entry.getKey() + ":](white) " + creditJoiner);
            }
        }

        if (!buttons.isEmpty()) {
            final StringJoiner buttonsJoiner = new StringJoiner("   ");
            for (final Link link : buttons) {
                buttonsJoiner.add("[[" + (link.icon != null ? link.icon + " " : "")
                                  + escapeMineDown(link.text) + "…]](" + link.color + " "
                                  + "show_text=&7Click to open link open_url=" + escapeMineDown(link.url) + ")");
            }
            menu.add("").add("[Links:](gray) " + buttonsJoiner);
        }

        return new MineDown(menu.toString()).replace();
    }

    /**
     * Return the plaintext string formatted menu. Use this for displaying to console
     *
     * @return The plaintext menu as a string
     */
    @Override
    @NotNull
    public String toString() {
        final StringJoiner menu = new StringJoiner("\n")
                .add(title + (version != null ? " | Version " + version : ""));
        if (description != null) {
            menu.add(description);
        }

        if (!attributions.entrySet().isEmpty()) {
            menu.add("━━━━━━━━━━━━━━━━━━━━━━━━");
        }
        for (final Map.Entry<String, List<Credit>> entry : attributions.entrySet()) {
            final StringJoiner creditJoiner = new StringJoiner(", ");
            for (final Credit credit : entry.getValue()) {
                creditJoiner.add(credit.name + (credit.description != null ? " (" + credit.description + ")" : ""));
            }

            if (!creditJoiner.toString().isBlank()) {
                menu.add("- " + entry.getKey() + ": " + creditJoiner);
            }
        }

        if (!buttons.isEmpty()) {
            menu.add("━━━━━━━━━━━━━━━━━━━━━━━━");
            for (final Link link : buttons) {
                menu.add("- " + link.text + ": " + link.url);
            }
        }

        return menu.toString();
    }

    /**
     * Represents a link related to the resource
     */
    public static class Link {
        @NotNull
        private String text = "Link";
        @NotNull
        private String color = "#00fb9a";
        @Nullable
        private String icon;
        @NotNull
        private final String url;

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
        public Link withText(@NotNull String text) {
            this.text = text;
            return this;
        }

        /**
         * Set the link button icon. Note this will not affect the string representation of the menu
         *
         * @param icon The icon character
         * @return The {@link Link}
         */
        public Link withIcon(@NotNull String icon) {
            this.icon = icon;
            return this;
        }

        /**
         * Set the link button color. Note this will not affect the string representation of the menu
         *
         * @param color The color
         * @return The {@link Link}
         */
        public Link withColor(@NotNull String color) {
            this.color = color;
            return this;
        }

    }

    /**
     * Represents information about someone who worked on the resource
     */
    public static class Credit {
        @NotNull
        private final String name;
        @Nullable
        private String description;
        @Nullable
        private String url;
        @NotNull
        private String color = "gray";

        private Credit(@NotNull String name) {
            this.name = name;
        }

        /**
         * Create a credit from someone's name
         *
         * @param name The name of the person to credit
         * @return The {@link Credit}
         */
        public static Credit of(@NotNull String name) {
            return new Credit(name);
        }

        /**
         * Set the description of the credit; i.e. what they did
         *
         * @param description The description of the credit (what they did)
         * @return The {@link Credit}
         */
        public Credit withDescription(@Nullable String description) {
            this.description = description;
            return this;
        }

        /**
         * Set the URL of the credit; i.e. their website. Note this has no effect on the string representation of the menu
         *
         * @param url The URL of the credit (i.e. their website)
         * @return The {@link Credit}
         */
        public Credit withUrl(@Nullable String url) {
            this.url = url;
            return this;
        }

        /**
         * Set the color of the credit. Note this has no effect on the string representation of the menu
         *
         * @param color The color of the credit
         * @return The {@link Credit}
         */
        public Credit withColor(@NotNull String color) {
            this.color = color;
            return this;
        }

    }

    /**
     * Escape a string from {@link MineDown} formatting for use in a MineDown-formatted locale
     * <p>
     * Although MineDown provides {@link MineDown#escape(String)}, that method fails to escape events
     * properly when using the escaped string in a replacement, so this is used instead
     *
     * @param string The string to escape
     * @return The escaped string
     */
    @NotNull
    private static String escapeMineDown(@NotNull String string) {
        final StringBuilder value = new StringBuilder();
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            boolean isEscape = c == '\\';
            boolean isColorCode = i + 1 < string.length() && (c == 167 || c == '&');
            boolean isEvent = c == '[' || c == ']' || c == '(' || c == ')';
            boolean isFormatting = (c == '_' || c == '*' || c == '~' || c == '?' || c == '#') && Util.isDouble(string, i);
            if (isEscape || isColorCode || isEvent || isFormatting) {
                value.append('\\');
            }

            value.append(c);
        }
        return value.toString();
    }

}
