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
