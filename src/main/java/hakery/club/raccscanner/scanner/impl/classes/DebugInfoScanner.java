package hakery.club.raccscanner.scanner.impl.classes;

import hakery.club.raccscanner.scanner.Scanner;
import hakery.club.raccscanner.util.SourceFileData;

import java.util.ArrayList;

public class DebugInfoScanner extends Scanner<ArrayList<SourceFileData>> {

    @Override
    public boolean scan() {
        ArrayList<SourceFileData> tmp = new ArrayList<>();
        rScanner.getClasses().forEach((name, node) -> {
            if (node.sourceDebug != null && node.sourceDebug.length() > 0) {

                rScanner.setDebugInfoFound(true);

                tmp.add(new SourceFileData(node, node.sourceDebug));

                if (rScanner.isDebugging()) {
                    System.out.println(String.format("[Raccoon] Found debug info %s -> %s", name, node.sourceDebug));
                }
            }
        });

        this.setResult(tmp);

        return true;
    }
}
