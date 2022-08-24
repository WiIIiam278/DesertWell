package net.william278.desertwell;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UpdateCheckerTests {

    @Test
    public void testUpdateChecker() {
        // Tests against the HuskSync resource ID
        final UpdateChecker updateChecker = UpdateChecker.create(Version.fromString("1.0.0"), 97144);
        Assertions.assertFalse(updateChecker.isUpToDate().join());
    }

}
