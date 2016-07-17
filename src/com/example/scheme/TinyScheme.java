package com.example.scheme;

import java.io.*;
import java.util.*;

public class TinyScheme {

    public static final String VERSION_STRING = "0.6";

    private Environment env = new Environment();

    private Map<Symbol, Cell> macro_table = new HashMap<Symbol, Cell>();

    private Tokenizer tokenizer =null;

    private String displayValue = null;

    private BufferedReader reader = null;
    private BufferedWriter writer = null;

    public static final String CR = System.getProperty("line.separator");

    private static Map<String, Symbol> quotes = new HashMap<String, Symbol>();
    static {
        quotes.put("'",  Symbol.create("quote"));
        quotes.put("`",  Symbol.create("quasiquote"));
        quotes.put(",",  Symbol.create("unquote"));
        quotes.put(",@", Symbol.create("unquotesplicing"));
    }

    public static void main(String args[]){
        if (args.length > 1){
            System.out.println("too much parameter!");
            System.exit(-1);
        }
        try {
            TinyScheme ts = new TinyScheme();
            if (args.length == 1){
                ts.initFile(args[0]);
            } else {
                ts.initStdin();
            }
            ts.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TinyScheme(){}

    public void initFile(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        reader = new BufferedReader(br);
        OutputStreamWriter os = new OutputStreamWriter(System.out);
        writer = new BufferedWriter(os);
        init();
        tokenizer.setPrompt(true);
        tokenizer.setEchoBack(true);
        tokenizer.init(reader, writer);
        write("ready.");
    }

    public void initStdin() throws IOException {
        reader = new BufferedReader( new InputStreamReader(System.in));
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
        init();
        tokenizer.setPrompt(true);
        tokenizer.setEchoBack(false);
        tokenizer.init(reader, writer);
        write("ready.");
    }

    public void init() throws IOException {
        write("## TinyScheme Ver " + VERSION_STRING + " ##");
        this.tokenizer = new Tokenizer();

        env.clear();

        env.put("+", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                Cell result = new Cell(0);
                for (Cell param : params) {
                    result.add(param);
                }
                return result;
            }
        }));
        env.put("-", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length == 0){
                    throw new RuntimeException("Bad Parameter:-");
                } else if (params.length == 1){
                    Cell result = new Cell(0);
                    result.subtract(params[0]);
                    return result;
                }

                Cell result = new Cell(params[0]);
                for(int i =1; i < params.length;i++){
                    result.subtract(params[i]);
                }
                return result;
            }
        }));
        env.put("*", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                Cell result = new Cell(1);
                for (Cell param : params) {
                    result.multiplｙ(param);
                }
                return result;
            }
        }));
        env.put("/", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                // Schemeでの「/」の動作と違い、分数を扱わ（え）ない
                if (params.length == 0){
                    throw new RuntimeException("Bad Parameter:/");
                } else if (params.length == 1){
                    Cell result = new Cell(1);
                    result.divide(params[0]);
                    return result;
                }
                Cell result = new Cell(params[0]);
                for(int i =1; i < params.length;i++){
                    if (params[i].getNumber() == 0){
                        throw new RuntimeException("Divide by zero:/");
                    }
                    result.divide(params[i]);
                }
                return result;
            }
        }));
        env.put("quotient", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 2){
                    throw new RuntimeException("Bad Parameter:quotient");
                }
                double no = params[0].getNumber();
                if (params[1].getNumber() == 0){
                    throw new RuntimeException("Divide by zero:quotient");
                }
                long result = (long)(no / params[1].getNumber());
                return new Cell(result);
            }
        }));
        env.put("modulo", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 2){
                    throw new RuntimeException("Bad Parameter:quotient");
                }
                double no = params[0].getNumber();
                if (params[1].getNumber() == 0){
                    throw new RuntimeException("Divide by zero:quotient");
                }
                long result = (long)no % (long)params[1].getNumber();
                return new Cell(result);
            }
        }));
        env.put("sqrt", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 1){
                    throw new RuntimeException("Bad Parameter:sqrt");
                }
                return new Cell(Math.sqrt(params[0].getNumber()));
            }
        }));
        env.put("not", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 1){
                    throw new RuntimeException("Bad Parameter:not");
                }
                return  new Cell(params[0].isFalse());
            }
        }));
        env.put(">", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length < 2){
                    return  new Cell(true);
                }
                boolean result = true;
                Cell target = params[0];
                for(int i =1; i < params.length;i++){
                    if (!result) break;
                    result = target.getNumber() > params[i].getNumber();
                    target = params[i];
                }
                return  new Cell(result);
            }
        }));
        env.put(">=", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length < 2){
                    return  new Cell(true);
                }
                boolean result = true;
                Cell target = params[0];
                for(int i =1; i < params.length;i++){
                    if (!result) break;
                    result = target.getNumber() >= params[i].getNumber();
                    target = params[i];
                }
                return  new Cell(result);
            }
        }));
        env.put("<", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length < 2){
                    return  new Cell(true);
                }
                boolean result = true;
                Cell target = params[0];
                for(int i =1; i < params.length;i++){
                    if (!result) break;
                    result = target.getNumber() < params[i].getNumber();
                    target = params[i];
                }
                return  new Cell(result);
            }
        }));
        env.put("<=", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length < 2){
                    return  new Cell(true);
                }
                boolean result = true;
                Cell target = params[0];
                for(int i =1; i < params.length;i++){
                    if (!result) break;
                    result = target.getNumber() <= params[i].getNumber();
                    target = params[i];
                }
                return  new Cell(result);
            }
        }));
        env.put("=", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length < 2){
                    return  new Cell(true);
                }
                boolean result = true;
                Cell target = params[0];
                for(int i =1; i < params.length;i++){
                    if (!result) break;
                    result = target.equals(params[i]);
                    target = params[i];
                }
                return  new Cell(result);
            }
        }));
        env.put("equal?", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length < 2){
                    throw new RuntimeException("Bad Parameter:equal?");
                }
                boolean result = true;
                Cell target = params[0];
                for(int i =1; i < params.length;i++){
                    if (!result) break;
                    result = target.equalsExactly(params[i]);
                    target = params[i];
                }
                return  new Cell(result);
            }
        }));
        env.put("eq?", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length < 2){
                    throw new RuntimeException("Bad Parameter:eq?");
                }
                boolean result = true;
                Cell target = params[0];
                for(int i =1; i < params.length;i++){
                    if (!result) break;
                    result = target.hashCode() == params[i].hashCode();
                    target = params[i];
                }
                return  new Cell(result);
            }
        }));
        env.put("and", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                Cell cell = new Cell(true);
                for (Cell now : params) {
                    if (now.isFalse()) {
                        return new Cell(false);
                    }
                    cell = now;
                }
                return cell;
            }
        }));
        env.put("or", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                for (Cell now : params) {
                    if (!now.isFalse()) {
                        return now;
                    }
                }
                return  new Cell(false);
            }
        }));
        env.put("cons", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 2){
                    throw new RuntimeException("Bad Parameter:cons");
                }
                List<Cell> resultList = new ArrayList<Cell>();
                resultList.add(params[0]);
                TinyScheme.this.addList(params[1], resultList);
                return  new Cell(resultList);
            }
        }));
        env.put("car", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 1){
                    throw new RuntimeException("Bad Parameter:car");
                }
                if (params[0].isEmptyList()){
                    throw new RuntimeException("empty list:cdr");
                }
                return  params[0].getList().get(0);
            }
        }));
        env.put("cdr", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 1){
                    throw new RuntimeException("Bad Parameter:cdr");
                }
                if (params[0].isEmptyList()){
                    return new Cell(new ArrayList<Cell>());
                }
                return  new Cell(params[0].getList().subList(1, params[0].getList().size()));
            }
        }));
        env.put("append", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length < 1){
                    throw new RuntimeException("Bad Parameter:append");
                }
                List<Cell> cellList = new ArrayList<Cell>();
                for (Cell now : params){
                    TinyScheme.this.addList(now, cellList);
                }

                return  new Cell(cellList);
            }
        }));
        env.put("list", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                List<Cell> cellList = new ArrayList<Cell>();
                Collections.addAll(cellList, params);
                return  new Cell(cellList);
            }
        }));
        env.put("list?", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 1){
                    throw new RuntimeException("Bad Parameter:list?");
                }
                return  new Cell(params[0].getType() == Cell.Type.List);
            }
        }));
        env.put("length", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 1){
                    throw new RuntimeException("Bad Parameter:length");
                }
                if (params[0].getType() != Cell.Type.List){
                    throw new RuntimeException("not list:" + params[0].getSymbol());
                }
                return  new Cell(params[0].getList().size());
            }
        }));
        env.put("null?", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 1){
                    throw new RuntimeException("Bad Parameter:null?");
                }
                if (params[0].getType() != Cell.Type.List){
                    throw new RuntimeException("not list:" + params[0].getSymbol());
                }
                return  new Cell(params[0].getList() == null || params[0].getList().isEmpty());
            }
        }));
        env.put("symbol?", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 1){
                    throw new RuntimeException("Bad Parameter:symbol?");
                }
                return  new Cell(params[0].getType() == Cell.Type.Symbol);
            }
        }));
        env.put("display", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 1){
                    throw new RuntimeException("Bad Parameter:display");
                }
                try {
                    //display.append(print(params[0]));
                    writer.write(print(params[0]));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return Cell.None();
            }
        }));
        env.put("call/cc", new Cell(new Operator() {
            @Override
            public Cell call(Cell... params) {
                if (params.length != 1){
                    throw new RuntimeException("Bad Parameter:call/cc");
                }

                final CallCC callCC = new CallCC();
                Cell callbackCell = new Cell(new Operator() {
                    @Override
                    public Cell call(Cell... params) {
                        if (params.length != 1){
                            throw new RuntimeException("Bad Parameter:call/cc#callback");
                        }
                        callCC.setRetVal(params[0]);
                        throw callCC;
                    }
                });

                try {
                    Procedure proc = params[0].getProcedure();
                    Environment localEnv = new Environment(proc.getEnv());
                    localEnv.put(proc.getVar().getList().get(0).getSymbol().toString(), callbackCell);

                    return eval(proc.getExp(), localEnv);
                } catch (CallCC callback) {
                    if (callback.hashCode() == callCC.hashCode()){
                        return callback.getRetVal();
                    }
                    throw callback;
                }
            }
        }));
    }

    private void parse() throws IOException {
         try {
            while(tokenizer.hasNextLine()){
                try {
                    Cell expanded = expand(read(), true);
                    String str = print(eval(expanded));
                    if (str.length() > 0){
                        writer.write(str);
                        writer.write(CR);
                        writer.flush();
                    }
                } catch (RuntimeException re){
                    writer.write(re.getMessage());
                    writer.write(CR);
                    writer.flush();
                }
            }
        } finally {
            if (reader != null){
                reader.close();
            }
            if (writer != null){
                writer.flush();
                writer.close();
            }
        }
    }

    public String parse(String line) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(line.getBytes("utf8"));
        reader = new BufferedReader( new InputStreamReader(bais));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writer = new BufferedWriter(new OutputStreamWriter(baos));

        StringBuilder resultBuffer = new StringBuilder();

        tokenizer.init(reader, writer);
        tokenizer.setPrompt(false);
        tokenizer.setEchoBack(false);
        try {
            while(tokenizer.hasNextLine()){
                try {
                    Cell expanded = expand(read(), true);
                    String str = print(eval(expanded));
                    if (str.length() > 0){
                        resultBuffer.append(str).append(CR);
                    }
                } catch (RuntimeException re){
                    re.printStackTrace();
                    writer.write(re.getMessage());
                    writer.write(CR);
                    writer.flush();
                }
            }
        } finally {
            if (reader != null){
                reader.close();
            }
            if (writer != null){
                writer.flush();
                this.displayValue = new String (baos.toByteArray());
                writer.close();
            }
        }
        return resultBuffer.toString().trim();
    }

    public String print(Cell cell) throws IOException {
        return print(cell, 0);
    }

    public String print(Cell cell, int depth) throws IOException {
        switch (cell.getType()){
            case None:
                return "";

            case Long:
                return String.valueOf(cell.getLongValue());
            case Double:
                return String.valueOf(cell.getDoubleValue());
           case Boolean:
                if (cell.isBooleanValue()){
                    return "#t";
                } else {
                    return "#f";
                }
            case String:
                return  "\"" + cell.getString() + "\"";
            case Symbol:
                return cell.getSymbol().toString();
            case List: {
                StringBuilder buffer = new StringBuilder();
                boolean first = true;
                for(Cell now : cell.getList()){
                    if (first){
                        first = false;
                     } else {
                        if (buffer.charAt(buffer.length() - 1) != ' ') buffer.append(" ");
                    }
                    String str = print(now, depth + 1);
                    if (str.length() > 0){
                        buffer.append(str);
                    }
                }
                if (buffer.length() > 0 && buffer.charAt(buffer.length() - 1) == ' ') buffer.deleteCharAt(buffer.length() - 1);
                    buffer.insert(0,"(");
                    buffer.append(")");
                return buffer.toString();
            }
        }
        return "";
    }

    public Cell read() throws IOException {
        String token = this.tokenizer.nextToken();
        return this.read_ahead(token);
    }

    private Cell read_ahead(String token) throws IOException {
        if (token.length() == 0 || token.charAt(0) == ';'){
            return Cell.None();
        } else if (token.equals("(")){
            List<Cell> L = new ArrayList<Cell>();
            while (true) {
                token = tokenizer.nextToken();
                if (token.equals(")")) {
                    return new Cell(L);
                }
                L.add(read_ahead(token));
            }
        } else if (token.equals(")")){
            throw  new RuntimeException("unexpected )");
        } else if (token.equals("'") || token.equals("`") || token.equals(",") || token.equals(",@")){
            List<Cell> L = new ArrayList<Cell>();
            Symbol symbol = quotes.get(token);
            L.add(new Cell(symbol));
            token = tokenizer.nextToken();
            L.add(read_ahead(token));
            return new Cell(L);
        } else if (token.length() == 0){
            throw  new RuntimeException("unexpected EOF in list");
        }

        return atom(token);
    }

    public Cell atom(String token){
        // ブール値
        if (token.equals("#t")){
            return new Cell(true);
        } else if (token.equals("#f")){
            return new Cell(false);
        } else if (token.charAt(0) == '"'){
            // 文字列
            String tmp = token.substring(1, token.length() - 1);
            return new Cell(tmp);
        }
        try {
            // 整数
            long longVal = Long.parseLong(token);
            return new Cell(longVal);
        } catch (NumberFormatException e) {
            try {
                // 小数
                double doubleVal = Double.parseDouble(token);
                return new Cell(doubleVal);
            } catch (NumberFormatException e1) {
                // シンボル
                return new Cell(Symbol.create(token));
            }
        }
    }

    public Cell eval(Cell cell) {
        return this.eval(cell, this.env);
    }

    public Cell eval(Cell cell, Environment env){
        while (true){
            switch (cell.getType()){
                case Symbol:
                    Cell findCell = env.find(cell.getSymbol().toString());
                    if (findCell == null){
                        throw new RuntimeException("Symbol not found:" + cell.getSymbol());
                    }
                    return findCell;
                case None:
                case Long:
                case Double:
                case Boolean:
                case String:
                    return cell;
            }
            // 予約命令か
            List<Cell> cells = cell.getList();
            Cell head = cells.get(0);

            if (head.equalsSymbolName("quote")) {
                return cells.get(1);
            } else if (head.equalsSymbolName("if")){
                Cell test = eval(cells.get(1), env);
                if (!test.isFalse()){
                    cell = cells.get(2);
                } else {
                    cell = cells.get(3);
                }
            } else if (head.equalsSymbolName("set!")){
                Cell param1 = cells.get(1);
                Cell param2 = eval(cells.get(2), env);
                Cell findCell = env.findAndPut(param1.getSymbol().toString(), param2);
                if (findCell == null){
                    throw new RuntimeException("Symbol not found:" + param1.getSymbol());
                }
                return Cell.None();
            } else if (head.equalsSymbolName("define")){
                Cell param1 = cells.get(1);
                Cell param2 = eval(cells.get(2), env);
                env.put(param1.getSymbol().toString(), param2);
                return Cell.None();
            } else if (head.equalsSymbolName("lambda")){
                Procedure procedure = new Procedure(cells.get(1), cells.get(2), env);
                return new Cell(procedure);
            } else if (head.equalsSymbolName("begin")) {
                for (int i = 1; i < cells.size(); i++) {
                    cell = eval(cells.get(i), env);
                }
            } else {
                // Operator/Procedure呼び出し
                Cell procCell = eval(head, env);
                if (procCell == null){
                    throw new RuntimeException("Operator is not found:" + head.getSymbol());
                }
                if (procCell.getType() != Cell.Type.Operator && procCell.getType() != Cell.Type.Procedure){
                    return cell;
                }

                List<Cell> paramList = new ArrayList<Cell>();
                for (int i = 1; i < cells.size(); i++){
                    paramList.add(eval(cells.get(i), env));
                }
                if (procCell.getType() == Cell.Type.Procedure){
                    cell = procCell.getProcedure().getExp();

                    List<Cell> keys = new ArrayList<Cell>();
                    keys.addAll(procCell.getProcedure().getVar().getList());
                    if (paramList.size() != keys.size()){
                        if ((keys.size() == 0 && paramList.size() > 0) || (keys.size() > paramList.size())){
                            throw new RuntimeException("Operator parameter count is not match");
                        }
                        // 引数の数が合わない場合は、最後をList扱いに
                        this.packParameter(keys, paramList);
                    }

                    env = new Environment(procCell.getProcedure().getEnv());
                    for (int i = 0; i < keys.size(); i++){
                        env.put(keys.get(i).getSymbol().toString(), paramList.get(i));
                    }
                } else {
                    return procCell.getOperator().call(paramList.toArray(new Cell[paramList.size()]));
                }
            }
        }
    }

    public Cell expand(Cell cell){
        return expand(cell, false);
    }

    public Cell expand(Cell cell, boolean topLevel){

        if (cell.getType() != Cell.Type.List){
            return cell;
        }

        List<Cell> cells = cell.getList();
        if (cells.size() == 0){
            return new Cell(new ArrayList<Cell>());
        }

        Cell head = cells.get(0);

        if (cells.size() == 3){
            if (cells.get(1).equalsSymbolName(".")){
                List<Cell> retList = new ArrayList<Cell>();
                retList.add(new Cell(Symbol.create("cons")));
                retList.add(cells.get(0));
                retList.add(cells.get(2));
                return new Cell(retList);
            }
        }

        if (head.equalsSymbolName("quote")) {
            if (cells.size() != 2) {
                throw new RuntimeException("Bad parameter:quote");
            }

            return cell;
        } else if (head.equalsSymbolName("if")) {
            if (cells.size() == 3) {
                cells.add(Cell.None());
            }
            if (cells.size() != 4) {
                throw new RuntimeException("Bad parameter:if");
            }

            List<Cell> retList = new ArrayList<Cell>();
            for (Cell tmpCell : cells) {
                retList.add(expand(tmpCell));
            }
            return new Cell(retList);
        } else if (head.equalsSymbolName("set!")) {
            if (cells.size() != 3) {
                throw new RuntimeException("Bad parameter:set!");
            }
            List<Cell> retList = new ArrayList<Cell>();
            retList.add(cells.get(0));
            if (cells.get(1).getType() != Cell.Type.Symbol){
                throw new RuntimeException("Bad parameter(not symbol):set!");
            }
            retList.add(cells.get(1));
            retList.add(expand(cells.get(2)));
            return new Cell(retList);
        } else if (head.equalsSymbolName("define") || head.equalsSymbolName("define-macro")) {
            if (cells.size() < 3) {
                throw new RuntimeException("Bad parameter:" + head.getSymbol());
            }

            List<Cell> retList = new ArrayList<Cell>();
            retList.add(cells.get(0));

            Cell v = cells.get(1);
            Cell body;
            if (cells.size() > 3){
                List<Cell> tmpList = new ArrayList<Cell>();
                tmpList.add(new Cell(Symbol.create("begin")));
                tmpList.addAll(cells.subList(2, cells.size()));
                body = new Cell(tmpList);
            } else {
                body = cells.get(2);
            }
            if (v.getType() == Cell.Type.List){
                retList.add(v.getList().get(0));
                Cell args = new Cell(v.getList().subList(1, v.getList().size()));

                List<Cell> lambdaList = new ArrayList<Cell>();
                lambdaList.add(new Cell(Symbol.create("lambda")));
                lambdaList.add(args);
                lambdaList.add(body);
                retList.add(new Cell(lambdaList));
                return expand(new Cell(retList), topLevel && head.equalsSymbolName("define-macro"));

            } else {
                if (cells.size() != 3) {
                    throw new RuntimeException("Bad parameter:" + head.getSymbol());
                }
                if (v.getType() != Cell.Type.Symbol) {
                    throw new RuntimeException("Bad parameter(not symbol):" + head.getSymbol());
                }
                Cell exp = expand(body);

                if (head.getSymbol().equals(Symbol.create("define-macro"))){
                    if (!topLevel){
                        throw new RuntimeException("define-macro only allowed at top level");
                    }
                    Cell proc = eval(exp);
                    if (proc.getType() != Cell.Type.Procedure){
                        throw new RuntimeException("macro must be a procedure");
                    }
                    macro_table.put(v.getSymbol(), proc);

                    return Cell.None();
                }

                retList.add(v);
                retList.add(exp);
                return new Cell(retList);
            }
        } else if (head.equalsSymbolName("begin")) {
            if (cells.size() == 1) {
                return Cell.None();
            }

            List<Cell> retList = new ArrayList<Cell>();
            retList.add(cells.get(0));
            for (int i = 1; i < cells.size(); i++){
                retList.add(expand(cells.get(i), topLevel));
            }
            return new Cell(retList);
        } else if (head.equalsSymbolName("lambda")) {
            if (cells.size() < 3) {
                throw new RuntimeException("Bad parameter:lambda");
            }

            List<Cell> retList = new ArrayList<Cell>();
            retList.add(cells.get(0));

            if (cells.get(1).getType() == Cell.Type.Symbol){
                // lambda a -> lambda (a)
                List<Cell> tmpList = new ArrayList<Cell>();
                tmpList.add(cells.get(1));
                retList.add(new Cell(tmpList));
            } else {
                retList.add(cells.get(1));
            }

            Cell exp;
            if (cells.size() > 3){
                List<Cell> expList = new ArrayList<Cell>();
                expList.add(new Cell(Symbol.create("begin")));
                for (int i = 2; i < cells.size(); i++){
                    expList.add(expand(cells.get(i)));
                }
                exp = new Cell(expList);
            } else {
                exp = expand(cells.get(2));
            }
            retList.add(exp);
            return new Cell(retList);
        } else if (head.equalsSymbolName("let")) {
            if (cells.size() < 3) {
                throw new RuntimeException("Bad parameter:let");
            }

            List<Cell> vars = new ArrayList<Cell>();
            List<Cell> values = new ArrayList<Cell>();
            for(Cell param : cells.get(1).getList()){
                if (param.getType() != Cell.Type.List && param.getList().get(0).getType() != Cell.Type.Symbol){
                    throw new RuntimeException("Bad parameter(not symbol):let");
                }
                vars.add(param.getList().get(0));
                values.add(param.getList().get(1));
            }

            List<Cell> lambdaList = new ArrayList<Cell>();
            lambdaList.add(new Cell(Symbol.create("lambda")));
            lambdaList.add(new Cell(vars));
            lambdaList.add(cells.get(2));

            List<Cell> retList = new ArrayList<Cell>();
            retList.add(new Cell(lambdaList));
            retList.addAll(values);
            return expand(new Cell(retList));
        } else if (head.equalsSymbolName("quasiquote")) {
            if (cells.size() != 2) {
                throw new RuntimeException("Bad parameter:`");
            }

            return expand_quasiquote(cells.get(1));

        } else if (head.getType() == Cell.Type.Symbol && macro_table.containsKey(head.getSymbol())) {
            List<Cell> paramList = cells.subList(1, cells.size());
            Cell procCell = macro_table.get(head.getSymbol());

            List<Cell> keys = new ArrayList<Cell>();
            if (procCell.getProcedure().getVar().getType() == Cell.Type.Symbol){
                keys.add(procCell.getProcedure().getVar());
            } else {
                for (Cell now : procCell.getProcedure().getVar().getList()){
                    if (now.getType() == Cell.Type.Symbol){
                        keys.add(now);
                    }
                }
            }
            if (paramList.size() != keys.size()){
                if ((keys.size() == 0 && paramList.size() > 0) || (keys.size() > paramList.size())){
                    throw new RuntimeException("Operator parameter count is not match");
                }
                // 引数の数が合わない場合は、最後をList扱いに
                this.packParameter(keys, paramList);
            }

            Environment localEnv = new Environment(env);
            for (int i = 0; i < keys.size(); i++){
                localEnv.put(keys.get(i).getSymbol().toString(), paramList.get(i));
            }

            Cell result = eval(procCell.getProcedure().getExp(), localEnv);

            return expand(result, topLevel);
        }

        List<Cell> retList = new ArrayList<Cell>();
        for(Cell nowCell: cells) {
            retList.add(expand(nowCell));
        }
        return new Cell(retList);
    }

    private Cell expand_quasiquote(Cell cell){
        if (cell.getType() != Cell.Type.List){
            List<Cell> retList = new ArrayList<Cell>();
            retList.add(new Cell(Symbol.create("quote")));
            retList.add(cell);
            return new Cell(retList);
        }

        List<Cell> cells = cell.getList();

        Cell head = cells.get(0);

        if (head.equalsSymbolName("unquote")) {
            // unquote
            if (cells.size() != 2) {
                throw new RuntimeException("Bad parameter:,");
            }
            return cells.get(1);
        } else if (head.getType() == Cell.Type.List && head.getList().get(0).equalsSymbolName("unquotesplicing")){
            // unquotesplicing
            if ( head.getList().size() != 2) {
                throw new RuntimeException("Bad parameter:,@");
            }
            List<Cell> retList = new ArrayList<Cell>();
            retList.add(new Cell(Symbol.create("append")));
            retList.add(head.getList().get(1));
            if (cells.size() > 1){
                retList.add(expand_quasiquote(new Cell(cells.subList(1, cells.size()))));
            }
            return new Cell(retList);
        } else {
            List<Cell> retList = new ArrayList<Cell>();
            retList.add(new Cell(Symbol.create("cons")));
            retList.add(expand_quasiquote(cells.get(0)));
            if (cells.size() > 1){
                retList.add(expand_quasiquote(new Cell(cells.subList(1, cells.size()))));
            } else {
                retList.add(Cell.None());
            }

            return new Cell(retList);
        }
    }

    private void addList(Cell cell, List<Cell> resultList){
        if (cell.getType() != Cell.Type.None){
            if (cell.getType() == Cell.Type.List){
                resultList.addAll(cell.getList());
            } else {
                resultList.add(cell);
            }
        }
    }

    private void packParameter(List<Cell> keys, List<Cell> params){
        if (keys.size() == params.size()){
            return;
        }

        List<Cell> tmpList = new ArrayList<Cell>();
        for (int i = keys.size() - 1; i < params.size(); i++){
            tmpList.add(params.get(i));
        }
        for(int i = params.size() - 1;i >= keys.size() - 1; i--){
            params.remove(i);
        }
        params.add(new Cell(tmpList));
    }

    public String getDisplayValue() {
        return this.displayValue;
    }

    public void write(String str){
        if (writer != null){
            try {
                writer.write(str);
                writer.write(CR);
                writer.flush();
            } catch (IOException e) {
               //
            }
        }
    }
}
