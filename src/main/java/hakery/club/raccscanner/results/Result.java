package hakery.club.raccscanner.results;

import hakery.club.raccscanner.Raccoon;
import hakery.club.raccscanner.results.util.Obfuscator;
import hakery.club.raccscanner.results.util.ObfuscatorResult;
import hakery.club.raccscanner.scanner.Scanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.allatori.AllatoriStringEncryptionScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.paramorphism.ParamorphismClassloaderScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.paramorphism.ParamorphismDecrypterScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.paramorphism.ParamorphismManifestScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.stringer.StringerHideAccessScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.stringer.StringerIntegrityControlScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.stringer.StringerManifestScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.stringer.StringerStringEncryptionScanner;

import java.util.ArrayList;
import java.util.Optional;

public class Result {

    ArrayList<ObfuscatorResult> results;
    Raccoon parent;

    public Result(Raccoon raccoon) {
        this.results = new ArrayList<>();
        this.parent = raccoon;

        /* fill temp reuslt lists */
        for (Obfuscator obfuscator : Obfuscator.values())
            this.results.add(new ObfuscatorResult(obfuscator));

        /** I'm sure this could be done better, but it's good for now */
        StringerHideAccessScanner hideAccessScanner = (StringerHideAccessScanner) getScanner(StringerHideAccessScanner.class);
        if (hideAccessScanner.getResult().size() > 0)
            getResultByObfuscator(Obfuscator.STRINGER).increasePercentage(20);

        StringerIntegrityControlScanner integrityControlScanner = (StringerIntegrityControlScanner) getScanner(StringerIntegrityControlScanner.class);
        if (integrityControlScanner.getResult().size() > 2)
            getResultByObfuscator(Obfuscator.STRINGER).increasePercentage(30);

        StringerManifestScanner stringerManifestScanner = (StringerManifestScanner) getScanner(StringerManifestScanner.class);
        if (stringerManifestScanner.getResult())
            getResultByObfuscator(Obfuscator.STRINGER).increasePercentage(50);

        StringerStringEncryptionScanner stringEncryptionScanner = (StringerStringEncryptionScanner) getScanner(StringerStringEncryptionScanner.class);
        if (stringEncryptionScanner.getResult().size() > 3)
            getResultByObfuscator(Obfuscator.STRINGER).increasePercentage(15);

        /** Only one for allatori, but never false-flags */
        AllatoriStringEncryptionScanner allatoriStringEncryptionScanner = (AllatoriStringEncryptionScanner) getScanner(AllatoriStringEncryptionScanner.class);
        if (allatoriStringEncryptionScanner.getResult().size() > 0)
            getResultByObfuscator(Obfuscator.ALLATORI).increasePercentage(50);

        ParamorphismManifestScanner paramorphismManifestScanner = (ParamorphismManifestScanner) getScanner(ParamorphismManifestScanner.class);
        if (paramorphismManifestScanner.getResult())
            getResultByObfuscator(Obfuscator.PARAMORPHISM).increasePercentage(50);

        ParamorphismClassloaderScanner paramorphismClassloaderScanner = (ParamorphismClassloaderScanner) getScanner(ParamorphismClassloaderScanner.class);
        if (paramorphismClassloaderScanner.getResult().size() > 1)
            getResultByObfuscator(Obfuscator.PARAMORPHISM).increasePercentage(30);

        ParamorphismDecrypterScanner paramorphismDecrypterScanner = (ParamorphismDecrypterScanner) getScanner(ParamorphismDecrypterScanner.class);
        if (paramorphismDecrypterScanner.getResult().size() > 3)
            getResultByObfuscator(Obfuscator.PARAMORPHISM).increasePercentage(15);
    }

    Scanner<?> getScanner(Class<? extends Scanner<?>> clazz) {
        Optional<Scanner<?>> scannerOptional = this.parent.getScanner().getScanners()
                .stream()
                .filter(scanner -> scanner.getClass() == clazz)
                .findAny();

        if (scannerOptional.isPresent())
            return scannerOptional.get();
        return null;
    }

    public ObfuscatorResult getResultByObfuscator(Obfuscator target) {
        Optional<ObfuscatorResult> obfuscatorResultOptional = this.results
                .stream()
                .filter(obf -> obf.getObfuscator() == target)
                .findAny();
        if (obfuscatorResultOptional.isPresent())
            return obfuscatorResultOptional.get();

        return null;
    }

    public void printResults() {
        this.results.forEach(obfuscatorResult -> this.parent.getLogger().log("Scanner result for %s = [%d%%]", obfuscatorResult.getObfuscator().toString(), obfuscatorResult.getPercentage()));
    }

}
