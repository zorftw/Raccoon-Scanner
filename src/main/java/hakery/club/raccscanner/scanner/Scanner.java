package hakery.club.raccscanner.scanner;

import hakery.club.raccscanner.RScanner;

public abstract class Scanner<T> {

    private T result;
    protected RScanner rScanner;

    public Scanner() {}

    public abstract boolean scan();

    public T getResult()
    {
        return this.result;
    }

    protected void setResult(T result)
    {
        this.result = result;
    }

}
