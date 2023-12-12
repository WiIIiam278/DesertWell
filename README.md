# DesertWell
![CI](https://img.shields.io/github/actions/workflow/status/WiIIiam278/DesertWell/ci.yml?branch=master&logo=github)
[![Discord](https://img.shields.io/discord/818135932103557162?color=7289da&logo=discord)](https://discord.gg/tVYhJfyDWG)
[![JitPack](https://jitpack.io/v/net.william278/DesertWell.svg)](https://jitpack.io/#net.william278/DesertWell)

**DesertWell** is a simple library providing various utilities to aid Minecraft plugin development on Adventure platforms. Requires Java 11+.

![Example of an about menu](images/about-menu-screenshot.png)

## Features
### About menus
`AboutMenu.class` allows for the generation of plugin about menus, as seen above.

To create an about menu, use `AboutMenu#create(title)` with the resource name, then use the various builder methods to build out the menu.

<details>
<summary>Displaying an AboutMenu</summary>

```java
public class ExamplePlugin extends JavaPlugin {
    
    // Displays the about menu to the player and logs it to console
    public void showAboutMenu(Player player) {

        final AboutMenu menu = AboutMenu.builder()
            .title(Component.text("Example"))
            .description(Component.text("An example plugin"))
            .version(plugin.getVersion())
            .credits("Author",
                    AboutMenu.Credit.of("William278").description("Click to visit website").url("https://william278.net"))
            .credits("Contributors",
                    AboutMenu.Credit.of("Contributor 1").description("Code, refactoring"))
            .credits("Translators",
                    AboutMenu.Credit.of("FreeMonoid").description("Italian (it-it)"),
                    AboutMenu.Credit.of("4drian3d").description("Coding"))
            .buttons(
                    AboutMenu.Link.of("https://william278.net/docs/velocitab").text("Docs").icon("⛏"),
                    AboutMenu.Link.of("https://discord.gg/tVYhJfyDWG").text("Discord").icon("⭐").color(TextColor.color(0x6773f5)))
            .build();


        // Display the menu to the player (Depending on your platform, you may need to get the adventure audience for the Player here instead)
        player.sendMessage(menu.toComponent());
        
        // Use #toString to get a console-friendly version of the menu
        getLogger().info(AboutMenu.toString());
    }

}
```
</details>

### Version
`Version.class` provides a simple way to compare semantic plugin and Minecraft versions. `VersionChecker.class` provides a utility for querying resources on different marketplaces (`SPIGOT`, `MODRINTH`, `POLYMART` and `GITHUB`) for the latest version of a plugin and comparing with the current version in order to check for updates.

<details>
<summary>Checking for updates</summary>

```java
public class ExamplePlugin extends JavaPlugin {

    // Checks for updates and logs to console
    public void checkForUpdates() {
        final UpdateChecker checker = UpdateChecker.builder()
                .currentVersion(getVersion())
                .endpoint(UpdateChecker.Endpoint.MODRINTH)
                .resource("velocitab")
                .build();
        checker.check().thenAccept(checked => {
            if (!checked.isUpToDate()) {
                getLogger().info("A new update is available: " + checked.getLatestVersion());
            }
        });
    }

}
```
</details>

## Usage
DesertWell is available on JitPack and requires Adventure. You can browse the Javadocs [here](https://javadoc.jitpack.io/net/william278/DesertWell/latest/javadoc/).

<details>
<summary>Adding the library to your project</summary>

First, add the JitPack repository to your `build.gradle`:
```groovy
repositories {
    maven { url 'https://repo.william278.net/snapshots/' }
}
```

Then add the dependency:
```groovy
dependencies {
    implementation 'net.william278:desertwell:2.0.4-SNAPSHOT'
}
```
</details>

### Maven & others
JitPack has a [handy guide](https://jitpack.io/#net.william278/DesertWell/#How_to) for how to use the dependency with other build platforms.

## License
DesertWell is licensed under Apache-2.0.
