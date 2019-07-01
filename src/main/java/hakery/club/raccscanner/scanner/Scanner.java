package hakery.club.raccscanner.scanner;

import hakery.club.raccscanner.RScanner;

public abstract class Scanner<T> {

    protected RScanner rScanner;
    private T result;

    public Scanner() {
    }

    public abstract boolean scan();

    public T getResult() {
        return this.result;
    }

    protected void setResult(T result) {
        this.result = result;
    }

}
