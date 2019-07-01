package an.innocent.program.main;

import hakery.club.raccscanner.RScanner;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        RScanner scanner = new RScanner(new File("input.jar"));
    }

}
