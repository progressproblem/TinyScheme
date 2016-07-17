package com.example.scheme;

public class Procedure {

    private Cell var = null;
    private Cell exp = null;
    private Environment env;

    protected Procedure(Cell var, Cell exp, Environment env) {
        this.var = var;
        this.exp = exp;
        this.env = env;
    }

    public Cell getVar() {
        return var;
    }

    public Cell getExp() {
        return exp;
    }

    public Environment getEnv() {
        return env;
    }
}
