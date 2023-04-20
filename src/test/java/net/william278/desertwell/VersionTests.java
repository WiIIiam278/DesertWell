package net.william278.desertwell;

import net.william278.desertwell.util.Version;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// Tests for the Version class
public class VersionTests {

    @Test
    public void testVersionParsing() {
        final Version version = Version.fromString("1.2.3");
        Assertions.assertEquals(1, version.getMajor());
        Assertions.assertEquals(2, version.getMinor());
        Assertions.assertEquals(3, version.getPatch());
        Assertions.assertEquals("", version.getMetadata());
    }

    @Test
    public void testVersionComparing() {
        final Version oldVersion = Version.fromString("1.0.0");
        final Version newVersion = Version.fromString("1.0.1");
        Assertions.assertTrue(oldVersion.compareTo(newVersion) < 0);
    }

    @Test
    public void testVersionComparingWithMetaDelimiter() {
        final Version oldVersion = Version.fromString("1.0.0+dev", "+");
        final Version newVersion = Version.fromString("1.0.1+snapshot-123", "+");
        Assertions.assertTrue(oldVersion.compareTo(newVersion) < 0);
    }

    @Test
    public void testParsingMinecraftVersion() {
        final Version version = Version.fromString("1.2.3-SNAPSHOT");
        Assertions.assertEquals(1, version.getMajor());
        Assertions.assertEquals(2, version.getMinor());
        Assertions.assertEquals(3, version.getPatch());
        Assertions.assertEquals("SNAPSHOT", version.getMetadata());
    }

}
