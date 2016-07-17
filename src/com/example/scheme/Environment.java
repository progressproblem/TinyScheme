package com.example.scheme;

import java.util.HashMap;
import java.util.Map;

public class Environment  {

    public Environment outer = null;

    private Map<String, Cell> map = new HashMap<String, Cell>();

    public Environment() {
        super();
    }

    public Environment( Environment outer) {
        super();
        this.outer = outer;
    }

    public Cell find(String val) {
        if (this.map.containsKey(val)){
            return this.map.get(val);
        } else {
            if (this.outer != null){
                return this.outer.find(val);
            }
            return null;
        }
    }

    public Cell get(String key){
        return this.map.get(key);
    }

    public Cell put(String key, Cell value){
        this.map.put(key, value);
        return value;
    }

    public Cell findAndPut(String key, Cell value){
        Environment nowEnv = this;
        while(true){
            if (nowEnv.map.containsKey(key)){
                nowEnv.map.put(key, value);
                return value;
            }
            if (nowEnv.outer == null){
                return Cell.None();
            }
            nowEnv = nowEnv.outer;
        }
    }

    public void clear(){
        this.map.clear();
    }
}
