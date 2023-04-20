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

import net.william278.desertwell.util.UpdateChecker;
import net.william278.desertwell.util.Version;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UpdateCheckerTests {

    @Test
    public void testSpigotEndpoint() {
        final UpdateChecker updateChecker = UpdateChecker.builder()
                .currentVersion(Version.fromString("1.0.0"))
                .resource("97144")
                .build();

        Assertions.assertFalse(updateChecker.check().join().isUpToDate());
    }

    @Test
    public void testGitHubEndpoint() {
        final UpdateChecker updateChecker = UpdateChecker.builder()
                .currentVersion(Version.fromString("1.0.0"))
                .endpoint(UpdateChecker.Endpoint.GITHUB)
                .resource("WiIIiam278/HuskHomes2")
                .build();

        Assertions.assertFalse(updateChecker.check().join().isUpToDate());
    }

    @Test
    public void testModrinthEndpoint() {
        final UpdateChecker updateChecker = UpdateChecker.builder()
                .currentVersion(Version.fromString("1.0.0"))
                .endpoint(UpdateChecker.Endpoint.MODRINTH)
                .resource("huskhomes")
                .build();

        Assertions.assertFalse(updateChecker.check().join().isUpToDate());
    }

    @Test
    public void testPolymartEndpoint() {
        final UpdateChecker updateChecker = UpdateChecker.builder()
                .currentVersion(Version.fromString("1.0.0"))
                .endpoint(UpdateChecker.Endpoint.POLYMART)
                .resource("284")
                .build();

        Assertions.assertFalse(updateChecker.check().join().isUpToDate());
    }

}
