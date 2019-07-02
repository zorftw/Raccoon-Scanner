package hakery.club.raccscanner.scanner;

import hakery.club.raccscanner.RScanner;
import hakery.club.raccscanner.scanner.impl.classes.ClassCountScanner;
import hakery.club.raccscanner.scanner.impl.classes.DebugInfoScanner;
import hakery.club.raccscanner.scanner.impl.jar.JarSizeScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.allatori.AllatoriStringEncryptionScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.paramorphism.ParamorphismClassloaderScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.paramorphism.ParamorphismDecrypterScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.stringer.StringerHideAccessScanner;
import hakery.club.raccscanner.scanner.impl.obfuscators.stringer.StringerStringEncryptionScanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scanners {

    private RScanner raccoonInstance;

    private ArrayList<Scanner<?>> scannerArrayList = new ArrayList<>();

    public Scanners(RScanner instance) {
        this.raccoonInstance = instance;

        add(Arrays.asList(
                /* classes */
                ClassCountScanner.class,
                DebugInfoScanner.class,

                /* debug */
                //DebugScanner.class,

                /* jar related stuff */
                JarSizeScanner.class,

                /* obfuscators */
                AllatoriStringEncryptionScanner.class,
                ParamorphismDecrypterScanner.class,
                ParamorphismClassloaderScanner.class,
                StringerStringEncryptionScanner.class,
                StringerHideAccessScanner.class
        ));
    }

    public void scan() {
        this.scannerArrayList.forEach(scanner -> new Thread(() -> {
            /* Might save performance using different threads */
            if (!scanner.scan())
                System.out.println(String.format("[Raccoon] scanner %s ended in failure", scanner.getClass().getName()));
        }).start());
    }

    private void add(List<Class<? extends Scanner<?>>> scanner) {
        scanner.forEach(scanner1 -> {
            try {
                Scanner instance = scanner1.newInstance();
                instance.rScanner = raccoonInstance;

                this.scannerArrayList.add(instance);
            } catch (Exception _) {
                _.printStackTrace();
            }
        });
    }

    public ArrayList<Scanner<?>> getScanners() {
        return scannerArrayList;
    }

}
