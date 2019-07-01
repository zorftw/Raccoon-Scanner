package hakery.club.raccscanner.scanner.impl.classes;

import hakery.club.raccscanner.scanner.Scanner;

public class ClassCountScanner extends Scanner<Integer> {

    @Override
    public boolean scan() {
        //TODO: Filter out invalid classes
        this.setResult(rScanner.getClasses().size());

        if (rScanner.isDebugging())
            System.out.printf("[Raccoon] Found %d classes\n", this.getResult());

        return true;
    }
}
