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
