package com.example.scheme;

public class CallCC extends RuntimeException {
    private Cell retVal = null;

    public CallCC() {
    }

    public Cell getRetVal() {
        return retVal;
    }

    public void setRetVal(Cell retVal) {
        this.retVal = retVal;
    }
}
