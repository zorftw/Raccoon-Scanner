package hakery.club.raccscanner.results.util;

/* basically a pair */
public class ObfuscatorResult {

    private Obfuscator obfuscator;
    private int percentage;

    public ObfuscatorResult(Obfuscator obfuscator) {
        this.obfuscator = obfuscator;
        this.percentage = 0;
    }

    public int getPercentage() {
        return Math.min(100, this.percentage);
    }

    public void increasePercentage(int percentage) {
        this.percentage += percentage;
    }

    public Obfuscator getObfuscator() {
        return this.obfuscator;
    }
}
