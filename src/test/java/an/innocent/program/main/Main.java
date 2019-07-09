package an.innocent.program.main;

import hakery.club.raccscanner.Raccoon;
import hakery.club.raccscanner.results.util.Obfuscator;
import hakery.club.raccscanner.results.util.result.impl.ParamorphismResult;
import hakery.club.raccscanner.results.util.result.impl.StringerResult;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        /**
         * Normal example on how to run the scanner
         *  1. Make new instance
         *  2. Initialize with an outputstream
         *  3. Scan!
         */
        Raccoon scanner = new Raccoon(new File("input.jar"));
        scanner.setDebugging(true); /* you can enable debugging */
        scanner.initialize(null); /* set output to System.out */
        scanner.scan(); /* if ur debugging this won't be necessary, as the scan will be done automatically instead */

        /**
         * Percentages can be easily written for you
         */
        scanner.getResult().printResults(); /* print all results */

        /**
         * Getting results is easy and simple
         */
        ParamorphismResult result = scanner.getResult().getObfuscatorResult(Obfuscator.PARAMORPHISM);
        scanner.getLogger().log("Paramorphism present in Manifest? [Y/N] %s", result.isPresentInManifest() ? "Y" : "N");

        int paramorphismPercentage = result.getPercentage();

        StringerResult stringerResult = scanner.getResult().getObfuscatorResult(Obfuscator.STRINGER);
        scanner.getLogger().log("String encryption entry size: %d", stringerResult.getStringEncryptionResult().size());
    }

}
