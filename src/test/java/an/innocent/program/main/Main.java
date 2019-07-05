package an.innocent.program.main;

import hakery.club.raccscanner.Raccoon;
import hakery.club.raccscanner.results.util.Obfuscator;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        /**
         * Normal example on how to run the scanner
         *  1. Make new instance
         *  2. Initialize with an outputstream
         *  3. Scan!
         */
        Raccoon scanner = new Raccoon(new File("D:\\RaccoonSamples\\Stringer\\stringintegritycontrol\\fizzbuzz_basic-2.jar"));
        scanner.initialize(null); /* set output to System.out */
        scanner.scan();

        /**
         * Getting results is easy
         */
        scanner.getResult().printResults(); /* print all results */

        /**
         * Get result for certain obfuscator
         *
         * Right now only implementations, but later you'll be able to count flags,
         * parse all results etc.
         */
        int stringerPercentage = scanner.getResult().getResultByObfuscator(Obfuscator.STRINGER).getPercentage();
    }

}
