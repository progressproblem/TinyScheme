package com.example.scheme;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Symbol {

    private String name;
    private static Map<String, Symbol> symbol_table = new ConcurrentHashMap<String, Symbol>();

    private Symbol(String name) {
        this.name = name;
    }

    public boolean equals(Symbol symbol) {
        return name.equals(symbol.toString());
    }

    public static Symbol create(String string){
        if (symbol_table.get(string) != null){
            return symbol_table.get(string);
        }

        Symbol symbol = new Symbol(string);
        symbol_table.put(string, symbol);
        return symbol;
    }

    @Override
    public String toString() {
        return name;
    }
}
