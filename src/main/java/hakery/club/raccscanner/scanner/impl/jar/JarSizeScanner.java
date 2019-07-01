package hakery.club.raccscanner.scanner.impl.jar;

import hakery.club.raccscanner.RScanner;
import hakery.club.raccscanner.scanner.Scanner;

public class JarSizeScanner extends Scanner<Long> {

    @Override
    public boolean scan() {
        this.setResult(rScanner.getTargetFile().length());

        if(rScanner.isDebugging())
            System.out.printf("[Raccoon] Jar is %o bytes\n", this.getResult());

        return true;
    }
}
