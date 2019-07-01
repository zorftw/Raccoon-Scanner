package hakery.club.raccscanner.results;

import hakery.club.raccscanner.RScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.paramorphism.ParamorphismClassloaderScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.paramorphism.ParamorphismDecrypterScanner;

import java.util.HashMap;
import java.util.Map;

public class Result {

    Map<String, Integer> obfuscatorPercentage = new HashMap<>();

    public Result(RScanner rScanner)
    {

        rScanner.getScanner().getScanners().forEach(scanner -> {
            if(scanner instanceof ParamorphismClassloaderScanner)
            {
                ParamorphismClassloaderScanner paramorphismClassloaderScanner = (ParamorphismClassloaderScanner)scanner;
                if(paramorphismClassloaderScanner.getResult().size() > 2)
                    obfuscatorPercentage.put("Paramorphism", obfuscatorPercentage.get("Paramorphism") != null
                            ? obfuscatorPercentage.get("Paramorphism") + 50
                            : 50
                    );
            }
            if(scanner instanceof ParamorphismDecrypterScanner)
            {
                ParamorphismDecrypterScanner paramorphismDecrypterScanner = (ParamorphismDecrypterScanner)scanner;
                if(paramorphismDecrypterScanner.getResult().size() > 2)
                    obfuscatorPercentage.put("Paramorphism", obfuscatorPercentage.get("Paramorphism") != null
                            ? obfuscatorPercentage.get("Paramorphism") + 25
                            : 25
                    );
            }
        });
    }

}
