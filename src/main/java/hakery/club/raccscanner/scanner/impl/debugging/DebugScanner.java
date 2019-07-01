package hakery.club.raccscanner.scanner.impl.debugging;

import hakery.club.raccscanner.scanner.Scanner;

public class DebugScanner extends Scanner<Integer> {

    @Override
    public boolean scan() {

        rScanner.getClasses().forEach((name, node) -> {
            System.out.println(name);

            node.methods.forEach(methodNode -> System.out.println(methodNode.name));
        });

        return true;
    }
}
