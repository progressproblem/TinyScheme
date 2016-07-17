package com.example.scheme;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    enum Type {None, Long, Double, Boolean, Symbol, Operator, Procedure, List, String};

    private Type type = Type.None;

    private long longValue = 0;
    private double doubleValue = 0.0D;
    private boolean booleanValue = false;
    private Symbol symbol = null;
    private String string = null;
    private List<Cell> list = new ArrayList<Cell>();
    private Operator operator = null;
    private Procedure procedure = null;

    private Cell() {
    }

    public Cell(long longValue) {
        this.longValue = longValue;
        this.type = Type.Long;
    }

    public Cell(double doubleValue) {
        this.doubleValue = doubleValue;
        this.type = Type.Double;
    }

    public Cell(boolean booleanValue) {
        this.booleanValue = booleanValue;
        this.type = Type.Boolean;
    }

    public Cell(Symbol symbol) {
        this.symbol = symbol;
        this.type = Type.Symbol;
    }

    public Cell(String string) {
        this.string = string;
        this.type = Type.String;
    }

    public Cell(Operator operator) {
        this.operator = operator;
        this.type = Type.Operator;
    }

    public Cell(Procedure procedure) {
        this.procedure = procedure;
        this.type = Type.Procedure;
    }

    public Cell(List<Cell> list) {
        this.list = list;
        this.type = Type.List;
    }

    public Cell(Cell cell) {
        this.type = cell.getType();
        switch (type){
            case Boolean:
                this.booleanValue = cell.booleanValue;
                break;
            case Long:
                this.longValue = cell.longValue;
                break;
            case Double:
                this.doubleValue = cell.doubleValue;
                break;
            case Symbol:
                this.symbol = cell.symbol;
                break;
            case String:
                this.string = cell.string;
                break;
            case List:
                this.list = cell.list;
                break;
        }
    }

    public Type getType() {
        return type;
    }

    public long getLongValue() {
        return longValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public List<Cell> getList() {
        return list;
    }

    public Operator getOperator() {
        return operator;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public boolean isBooleanValue()
    {
        return booleanValue;
    }

    public String getString() {
        return string;
    }

    public double getNumber() {
        switch (type) {
            case Boolean:
                return this.booleanValue ? 1 : 0;
            case Double:
                return this.doubleValue;
            case Long:
                return this.longValue;
        }
        return 0;
    }

    public boolean equalsExactly(Cell cell) {
        return _equals(cell, true);
    }

    public boolean equals(Cell cell) {
        return _equals(cell, false);
    }

    private boolean _equals(Cell cell, boolean flag) {
        switch (type){
            case Boolean:
                return cell.type == Type.Boolean && this.booleanValue == cell.isBooleanValue();
            case Long:
                if (flag){
                    return cell.type == Type.Long && this.longValue == cell.getLongValue();
                } else {
                    return (cell.type == Type.Long || cell.type == Type.Double)&& this.getNumber() == cell.getNumber();
                }
            case Double:
                if (flag){
                    return cell.type == Type.Double && this.doubleValue == cell.getDoubleValue();
                } else {
                    return (cell.type == Type.Long || cell.type == Type.Double)&& this.getNumber() == cell.getNumber();
                }
            case Symbol:
                return cell.type == Type.Symbol && this.symbol.equals(cell.getSymbol());
            case String:
                return cell.type == Type.String && this.string.equals(cell.getString());
            case List:
                if (cell == null || cell.type != Type.List ){
                    return false;
                }
                if (this.list.size() != cell.getList().size()){
                    return false;
                }
                boolean result = true;
                for (int i = 0; i < this.list.size(); i++){
                    if (!result)  {
                        break;
                    }
                    result = this.list.get(i)._equals(cell.getList().get(i), flag);
                }
                return result;

        }
        return false;
    }

    public Cell getValue() {
        switch (type){
            case Boolean:
            case Long:
            case Double:
                return this;
            case Symbol:
                if (this.symbol != null && this.symbol.toString().length() > 0){
                    return this;
                }
                break;
            case String:
                if (this.string != null && this.string.length() > 0){
                    return this;
                }
                break;
            case List:
                if (this.list.size() > 0){
                    return this;
                }

        }
        return new Cell(false);
    }

    public boolean isFalse(){
        Cell cell = this.getValue();
        return cell.getType() == Cell.Type.Boolean && !cell.isBooleanValue();
    }

    public void add(Cell cell){
        if (this.type == Type.Long && cell.getType() == Type.Long){
            this.longValue += cell.getLongValue();
        } else if (this.type == Type.Long && cell.getType() == Type.Double){
            this.type = Type.Double;
            this.doubleValue = (double)this.longValue;
            this.doubleValue += cell.getDoubleValue();
        } else if (this.type == Type.Double && cell.getType() == Type.Long){
            this.doubleValue += (double)cell.getLongValue();
        } else if (this.type == Type.Double && cell.getType() == Type.Double){
            this.doubleValue += cell.getDoubleValue();
        }
    }

    public void subtract(Cell cell){
        if (this.type == Type.Long && cell.getType() == Type.Long){
            this.longValue -= cell.getLongValue();
        } else if (this.type == Type.Long && cell.getType() == Type.Double){
            this.type = Type.Double;
            this.doubleValue = (double)this.longValue;
            this.doubleValue -= cell.getDoubleValue();
        } else if (this.type == Type.Double && cell.getType() == Type.Long){
            this.doubleValue -= (double)cell.getLongValue();
        } else if (this.type == Type.Double && cell.getType() == Type.Double){
            this.doubleValue -= cell.getDoubleValue();
        }
    }

    public void multiplｙ(Cell cell){
        if (this.type == Type.Long && cell.getType() == Type.Long){
            this.longValue *= cell.getLongValue();
        } else if (this.type == Type.Long && cell.getType() == Type.Double){
            this.type = Type.Double;
            this.doubleValue = (double)this.longValue;
            this.doubleValue *= cell.getDoubleValue();
        } else if (this.type == Type.Double && cell.getType() == Type.Long){
            this.doubleValue *= (double)cell.getLongValue();
        } else if (this.type == Type.Double && cell.getType() == Type.Double){
            this.doubleValue *= cell.getDoubleValue();
        }
    }

    public void divide(Cell cell){
        if (this.type == Type.Long && cell.getType() == Type.Long){
            double tmp = (double)this.longValue / (double)cell.getLongValue();
            if (Math.floor(tmp) == tmp){
                this.longValue /= cell.getLongValue();
            } else {
                this.type = Type.Double;
                this.doubleValue = (double)this.longValue;
                this.doubleValue /= (double)cell.getLongValue();
            }
        } else if (this.type == Type.Long && cell.getType() == Type.Double){
            this.type = Type.Double;
            this.doubleValue = (double)this.longValue;
            this.doubleValue /= cell.getDoubleValue();
        } else if (this.type == Type.Double && cell.getType() == Type.Long){
            this.doubleValue /= (double)cell.getLongValue();
        } else if (this.type == Type.Double && cell.getType() == Type.Double){
            this.doubleValue /= cell.getDoubleValue();
        }
    }

    public boolean isEmptyList(){
        return type == Type.List && list.size() == 0;
    }

    public boolean equalsSymbolName(String name){
        return this.type == Type.Symbol && this.symbol == Symbol.create(name);
    }

    public static Cell None(){
        return new Cell();
    }
}