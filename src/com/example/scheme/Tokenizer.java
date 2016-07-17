package com.example.scheme;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    public static final String token_regs = "\\s*(,@|[('`,)]|\"(?:[\\\\].|[^\\\\\"])*\"|;.*|[^\\s('\"`,;)]*)(.*)";

    private String line = "";

    private Pattern regs_pattern = null;

    private BufferedReader reader = null;

    private boolean readEnd = false;

    private BufferedWriter writer = null;

    private boolean prompt = true;

    private boolean echoBack = false;

    public Tokenizer() throws IOException {
        this.regs_pattern = Pattern.compile(token_regs);
    }

    public void init(BufferedReader reader, BufferedWriter writer) throws IOException {
        this.reader = reader;
        this.readEnd = false;

        this.writer = writer;
    }

    public String nextToken() throws IOException {
        while(true){
            if (this.line.length() == 0){
                if (prompt){
                    writer.write(">");
                    writer.flush();
                }
                this.line = this.reader.readLine();
                if (this.line == null || this.line.equalsIgnoreCase("(exit)")){
                    this.readEnd = true;
                    this.line = "";
                    return  "";
                }

                if (echoBack){
                    writer.write(this.line);
                    writer.write(TinyScheme.CR);
                    writer.flush();
                }
            }
            if (this.line.length() == 0){
                return "";
            }

            Matcher m = this.regs_pattern.matcher(this.line);
            if (m.find()){
                String token = m.group(1);
                this.line = m.group(2);
                return token;
            }
        }
    }

    public boolean hasNextLine(){
        return !readEnd;
    }

    public boolean isPrompt() {
        return prompt;
    }

    public void setPrompt(boolean prompt) {
        this.prompt = prompt;
    }

    public boolean isEchoBack() {
        return echoBack;
    }

    public void setEchoBack(boolean echoBack) {
        this.echoBack = echoBack;
    }
}
