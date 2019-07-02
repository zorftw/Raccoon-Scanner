package hakery.club.raccscanner.scanner.impl.obfuscators.paramorphism;

import hakery.club.raccscanner.scanner.Scanner;
import hakery.club.raccscanner.util.DataUtils;

/**
 * Paramorphism leaves
 * "Obfuscated by: Paramorphism" in the manifest, can be removed
 */
public class ParamorphismManifestScanner extends Scanner<Boolean> {
    @Override
    public boolean scan() {

        if (raccoon.getJarManifest() != null) {
            byte[] paramorphismByteArray = "Paramorphism".getBytes();
            if (DataUtils.INSTANCE.findInByteArray(paramorphismByteArray, raccoon.getJarManifest())) {
                log("Paramorphism was found in Manifest file.");
            }
        }

        return raccoon.getJarManifest() != null;
    }
}
