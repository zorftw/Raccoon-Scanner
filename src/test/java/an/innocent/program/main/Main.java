package an.innocent.program.main;

import hakery.club.raccscanner.Raccoon;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        /**
         * Normal example on how to run the scanner
         *  1. Make new instance
         *  2. Initialize with an outputstream
         *  3. Scan!
         */
        Raccoon scanner = new Raccoon(new File("D:\\Paramorphism\\Hello-World-obf.jar"));
        scanner.initialize(null); /* set output to System.out */
        scanner.scan();
    }

}
