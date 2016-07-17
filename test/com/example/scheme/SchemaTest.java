package com.example.scheme;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SchemaTest {
    @Test
    public void testCalc() throws Exception {
        try {
            TinyScheme ts = new TinyScheme();
            ts.init();
            System.out.println("四則演算");
            {
                String answer = ts.parse("486");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "486");
            }
            {
                String answer = ts.parse("(+)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "0");
            }
            {
                String answer = ts.parse("(+ 1)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "1");
            }
            {
                String answer = ts.parse("(- 3)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "-3");
            }
            {
                String answer = ts.parse("(*)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "1");
            }
            {
                // 分数には非対応。通常の割り算として動作
                String answer = ts.parse("(/ 2)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "0.5");
            }
            {
                String answer = ts.parse("(+ 1 2 3)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "6");
            }
            {
                String answer = ts.parse("(- 10 3 5)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "2");
            }
            {
                String answer = ts.parse("(* 2 3 4)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "24");
            }
            {
                // 分数には非対応。通常の割り算として動作
                String answer = ts.parse("(/ 30 5 3)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "2");
            }
            {
                String answer = ts.parse("(* (+ 2 3) (- 5 3))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "10");
            }
            {
                String answer = ts.parse("(* 34 67)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "2278");
            }
            {
                String answer = ts.parse("(* 34 67.0)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "2278.0");
            }
            {
                String answer = ts.parse("(quotient 7 3)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "2");
            }
            {
                String answer = ts.parse("(modulo 7 3)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "1");
            }

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testBool() throws Exception {
        try {
            TinyScheme ts = new TinyScheme();
            ts.init();
            System.out.println("判定");
            {
                String answer = ts.parse("#t");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("#f");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#f");
            }
            {
                String answer = ts.parse("(and #f 0)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#f");
            }
            {
                String answer = ts.parse("(and 1 2 3)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
            {
                String answer = ts.parse("(and 1 2 3 #f)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#f");
            }
            {
                String answer = ts.parse("(or #f 0)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "0");
            }
            {
                String answer = ts.parse("(or 1 2 3)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "1");
            }
            {
                String answer = ts.parse("(or 1 2 3)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "1");
            }
            {
                String answer = ts.parse("(or #f #f #f)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#f");
            }
            {
                String answer = ts.parse("(define str \"hello\")(eq? str str)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(eq? \"hello\" \"hello\")");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#f");
            }
            {
                // 数値・リスト・文字列問わずequal?で判定
                String answer = ts.parse("(equal? 1.0 1.0)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                // 数値・リスト・文字列問わずequal?で判定
                String answer = ts.parse("(equal? 1 1.0)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#f");
            }
            {
                // 数値・リスト・文字列問わずequal?で判定
                String answer = ts.parse("(equal? (list 1 2 3) (list 1 2 3))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                // 数値・リスト・文字列問わずequal?で判定
                String answer = ts.parse("(equal? \"hello\" \"hello\")");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(= 1 1 1.0)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(< 1 2 3)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(< 1)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(<)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(= 2 2 2)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(< 2 3 3.1)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(> 4 1 -0.2)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(<= 1 1 1.1)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(>= 2 1 1.0)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(< 3 4 3.9)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#f");
            }
            {
                String answer = ts.parse("(not #f)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse("(not #t)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#f");
            }
            {
                String answer = ts.parse("(if #t \"TRUE\" \"FALSE\")");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "\"TRUE\"");
            }
            {
                String answer = ts.parse("(if #f \"TRUE\" \"FALSE\")");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "\"FALSE\"");
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }


    @Test
    public void testList() throws Exception {
        try {
            TinyScheme ts = new TinyScheme();
            ts.init();

            System.out.println("リスト操作");
            {
                String answer = ts.parse("(cdr '(a))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "()");
            }
            {
                String answer = ts.parse("(car '((a b) (c d) (e f)))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(a b)");
            }
            {
                String answer = ts.parse("(car (car '((a b) (c d) (e f))))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "a");
            }
            {
                String answer = ts.parse("(car (cdr '((a b) (c d) (e f))))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(c d)");
            }
            {
                // 「.」は左辺に右辺を単純に連結
                String answer = ts.parse("(1 . 2)");
                System.out.println("answer:[" + answer + "]");
                //assertEquals(answer, "(1 . 2)"); // こうならずに
                assertEquals(answer, "(1 2)"); // こうなる
            }
            {
                // 「cons m n」はmにnを単純に連結
                String answer = ts.parse("(cons 1 2)");
                System.out.println("answer:[" + answer + "]");
                //assertEquals(answer, "(1 . 2)"); // こうならずに
                assertEquals(answer, "(1 2)"); // こうなる
            }
            {
                String answer = ts.parse("(cons 'a 'b)");
                System.out.println("answer:[" + answer + "]");
//                assertEquals(answer, "(a . b)"); // こうならずに
                assertEquals(answer, "(a b)"); // こうなる
            }
            {
                String answer = ts.parse("(cons '(a b) '(c d))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "((a b) c d)");
            }
            {
                String answer = ts.parse("(cons 3 (cons 1 2))");
                System.out.println("answer:[" + answer + "]");
                //assertEquals(answer, "(3 1 . 2)"); // こうならずに
                assertEquals(answer, "(3 1 2)"); // こうなる
            }
            {
                String answer = ts.parse("(cons #t (cons 3 \"hello\"))");
                System.out.println("answer:[" + answer + "]");
                //assertEquals(answer, "(#t 3 . \"hello\")"); // こうならずに
                assertEquals(answer, "(#t 3 \"hello\")"); // こうなる
            }
            // ちゃんと動かない
//            {
//                String answer = ts.parse("'(a . (b . (c . ())))");
//                System.out.println("answer:[" + answer + "]");
//                assertEquals(answer, "(a b c)");
//            }
            // consで繋げば動く
            {
                String answer = ts.parse("(cons 'a (cons 'b 'c))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(a b c)");
            }
            // ちゃんと動かない
//            {
//                String answer = ts.parse("'((a . (b . ())) . (c . (d . ())))");
//                System.out.println("answer:[" + answer + "]");
//                assertEquals(answer, "((a b) c d)");
//            }
            // consで繋げば動く
            {
                String answer = ts.parse("(cons (cons 'a  'b)  (cons 'c 'd))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "((a b) c d)");
            }
            {
                String answer = ts.parse("(append '((a b) (c d)) '(e f))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "((a b) (c d) e f)");
            }
            {
                String answer = ts.parse("(append '((a b) (c d)) '((e f)))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "((a b) (c d) (e f))");
            }
            {
                String answer = ts.parse("(append '(a b) '(c d) '(e f))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(a b c d e f)");
            }
            {
                String answer = ts.parse("(append '(a) '(b c) '(d e) '(f g))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(a b c d e f g)");
            }
            {
                String answer = ts.parse("(list)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "()");
            }
            {
                String answer = ts.parse("(list 1)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(1)");
            }
            {
                String answer = ts.parse("(list '(1 2) '(3 4))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "((1 2) (3 4))");
            }
            {
                String answer = ts.parse("(list 0)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(0)");
            }
            {
                String answer = ts.parse("(list 1 2)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(1 2)");
            }
            {
                String answer = ts.parse("(cons \"hi\" \"everybody\")");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(\"hi\" \"everybody\")");
            }
            {
                String answer = ts.parse("(cons 0 '())");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(0)");
            }
            {
                String answer = ts.parse("(cons 1 (cons 10 100))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(1 10 100)");
            }
            {
                String answer = ts.parse("(cons 1 (cons 10 (cons 100 '())))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(1 10 100)");
            }
            {
                String answer = ts.parse("(cons 1 (cons 10 (cons 100 '())))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(1 10 100)");
            }
            {
                String answer = ts.parse("(define a 1)(define b 2)(define c 3)`(a b c)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(a b c)");
            }
            {
                String answer = ts.parse("(define a 1)(define b 2)(define c 3)`(a b c)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(a b c)");
            }
            {
                String answer = ts.parse("(define d '(1 2 3))`(a ,d c)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(a (1 2 3) c)");
            }
            {
                String answer = ts.parse("`(a ,@d c)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(a 1 2 3 c)");
            }
            {
                String answer = ts.parse("(define L (list 1 2 3))" +
                        "`(testing ,@L testing)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(testing 1 2 3 testing)");
            }
            {
                String answer = ts.parse("`(testing ,L testing)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(testing (1 2 3) testing)");
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testOperator() throws Exception {
        try {
            TinyScheme ts = new TinyScheme();
            ts.init();

            System.out.println("基本操作");
            {
                String answer = ts.parse("(begin \"one\" \"two\")");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "\"two\"");
            }
            {
                String answer = ts.parse("(begin)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testVariable() throws Exception {
        try {
            TinyScheme ts = new TinyScheme();
            ts.init();

            System.out.println("代入");
            {
                String answer = ts.parse("(define var 1)var");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "1");
            }
            {
                String answer = ts.parse("(set! var (* var 10))var");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "10");
            }
            {
                String answer = ts.parse("((lambda (a b) (+ a b)) 1 2)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
            {
                String answer = ts.parse(
                        "(define bank-account"+
                            "(let ((amount 1000))"+
                                "(lambda (n)"+
                                    "(set! amount (+ amount n))"+
                                    "amount)))"+
                        "(bank-account 2000) "
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3000");
            }
            {
                String answer = ts.parse("(bank-account -2500)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "500");
            }
            {
                String answer = ts.parse(
                        "(define (make-bank-account amount)"+
                            "(lambda (n)"+
                                "(set! amount (+ amount n))"+
                                "amount))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse("(define saito-bank-account (make-bank-account 10000))(saito-bank-account -7000)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3000");
            }
            {
                String answer = ts.parse("(saito-bank-account 30000)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "33000");
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testFunction() throws Exception {
        try {
            TinyScheme ts = new TinyScheme();
            ts.init();

            System.out.println("関数");
            {
                String answer = ts.parse("(define fhello (lambda () \"Hello world\"))(fhello)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "\"Hello world\"");
            }
            {
                String answer = ts.parse("(define area (lambda (r) (* 3.141592653 (* r r))))(area 3)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "28.274333877");
            }
            System.out.println("省略形関数");
            {
                String answer = ts.parse("(define (sum3 a b c)(+ a b c))(sum3 1 2 3)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "6");
            }
            System.out.println("局所変数");
            {
                String answer = ts.parse("(let ((a 1) (b 2)) (+ a b))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
            {
                String answer = ts.parse("((lambda (a b) (+ a b)) 1 2 )");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
            {
                String answer = ts.parse("(define x 10)(let ((x 100)) (display x))");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
                System.out.println("display:[" + ts.getDisplayValue() + "]");
                assertEquals(ts.getDisplayValue(), "100");
            }
            {
                String answer = ts.parse("(define x 10)((lambda (x) (display x)) 100)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
                System.out.println("display:[" + ts.getDisplayValue() + "]");
                assertEquals(ts.getDisplayValue(), "100");
            }
            {
                String answer = ts.parse("x");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "10");
            }
            {
                String answer = ts.parse(
                                "(define (pow2 x y)"+
                                    "(if (= y 0)"+
                                    "1"+
                                    "(let ((z (pow2 x (quotient y 2))))"+
                                        "(if (= (modulo y 2) 0)"+
                                            "(* z z)"+
                                            "(* x z z)))))"+
                                "(pow2 2 3)"
                        );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "8");
            }
            {
                String answer = ts.parse("(pow2 3 2)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "9");
            }
            {
                String answer = ts.parse("(pow2 2 8)");
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "256");
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testMacro() throws Exception {
        try {
            TinyScheme ts = new TinyScheme();
            ts.init();

            System.out.println("マクロ");
            {
                String answer = ts.parse(
                        "(define-macro (first x) `(car ,x))"+
                        "(first '(a b c))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "a");
            }
            {
                String answer = ts.parse(
                        "(define-macro (m-square x) (list '* x x))"+
                        "(m-square (+ 1 2))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "9");
            }
            {
                String answer = ts.parse(
                        "(define-macro inc (lambda x `(set! ,x (+ ,x 1))))"+
                        "(define y 2)"+
                        "y"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "2");
            }
            {
                String answer = ts.parse(
                        "(inc y)y"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
            {
                String answer = ts.parse(
//                        "(define-macro unless (lambda args `(if (not ,(car args)) (begin ,@(cdr args)))))"
                        "(define-macro (unless args) `(if (not ,(car args)) (begin ,@(cdr args))))"

                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(unless (= 2 (+ 1 1)) (display 2) 3 4)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(unless (= 4 (+ 1 1)) (display 2) (display \"\\n\") 3 4)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "4");
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testCallCC() throws Exception {
        try {
            TinyScheme ts = new TinyScheme();
            ts.init();

            System.out.println("Call/CC");
            {
                String answer = ts.parse(
                        "(call/cc (lambda (throw)"+
                            "(+ 5 (* 10 (call/cc (lambda (escape) (* 100 (escape 3))))))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "35");
            }
            {
                String answer = ts.parse(
                        "(call/cc (lambda (throw)"+
                            "(+ 5 (* 10 (call/cc (lambda (escape) (* 100 (throw 3))))))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testLispy() throws Exception {
        try {
            TinyScheme ts = new TinyScheme();
            ts.init();

            System.out.println("Lispy Test case");

            {
                String answer = ts.parse(
                        "(quote (testing 1 (2.0) -3.14E159))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(testing 1 (2.0) -3.14E159)");
            }
            {
                String answer = ts.parse(
                        "(+ 2 2)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "4");
            }
            {
                String answer = ts.parse(
                        "(+ (* 2 100) (* 1 10))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "210");
            }
            {
                String answer = ts.parse(
                        "(if (> 6 5) (+ 1 1) (+ 2 2))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "2");
            }
            {
                String answer = ts.parse(
                        "(if (< 6 5) (+ 1 1) (+ 2 2))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "4");
            }
            {
                String answer = ts.parse(
                        "(define x 3)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "x"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
            {
                String answer = ts.parse(
                        "(+ x x)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "6");
            }
            {
                String answer = ts.parse(
                        "(begin (define x 1) (set! x (+ x 1)) (+ x 1))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
            {
                String answer = ts.parse(
                        "((lambda (x) (+ x x)) 5)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "10");
            }
            {
                String answer = ts.parse(
                        "(define twice (lambda (x) (* 2 x)))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(twice 5)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "10");
            }

            {
                String answer = ts.parse(
                        "(define compose (lambda (f g) (lambda (x) (f (g x)))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "((compose list twice) 5)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(10)");
            }
            {
                String answer = ts.parse(
                        "(define repeat (lambda (f) (compose f f)))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "((repeat twice) 5)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "20");
            }
            {
                String answer = ts.parse(
                        "((repeat (repeat twice)) 5)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "80");
            }
            {
                String answer = ts.parse(
                        "(define fact (lambda (n) (if (<= n 1) 1 (* n (fact (- n 1))))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(fact 3)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "6");
            }
// 数がでかすぎて未対応
//            {
//                String answer = ts.parse(
//                        "(fact 50)"
//                );
//                System.out.println("answer:[" + answer + "]");
//                assertEquals(answer, "30414093201713378043612608166064768844377641568960512000000000000");
//            }
            {
                String answer = ts.parse(
                        "(define abs (lambda (n) ((if (> n 0) + -) 0 n)))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(list (abs -3) (abs 0) (abs 3))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(3 0 3)");
            }
            {
                String answer = ts.parse(
                        "(define combine (lambda (f)"+
                            "(lambda (x y)"+
                                "(if (null? x) (quote ())"+
                                    "(f (list (car x) (car y))"+
                                        "((combine f) (cdr x) (cdr y)))))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(define zip (combine cons))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(zip (list 1 2 3 4) (list 5 6 7 8))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "((1 5) (2 6) (3 7) (4 8))");
            }
            {
                String answer = ts.parse(
                        "(define riff-shuffle (lambda (deck) (begin"+
                            "(define take (lambda (n seq) (if (<= n 0) (quote ()) (cons (car seq) (take (- n 1) (cdr seq))))))"+
                            "(define drop (lambda (n seq) (if (<= n 0) seq (drop (- n 1) (cdr seq)))))"+
                            "(define mid (lambda (seq) (/ (length seq) 2)))"+
                            "((combine append) (take (mid deck) deck) (drop (mid deck) deck)))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(riff-shuffle (list 1 2 3 4 5 6 7 8))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(1 5 2 6 3 7 4 8)");
            }
            {
                String answer = ts.parse(
                        "((repeat riff-shuffle) (list 1 2 3 4 5 6 7 8))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(1 3 5 7 2 4 6 8)");
            }
            {
                String answer = ts.parse(
                        "(riff-shuffle (riff-shuffle (riff-shuffle (list 1 2 3 4 5 6 7 8))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(1 2 3 4 5 6 7 8)");
            }
            {
                String answer = ts.parse(
                        "(define lyst (lambda items items))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(lyst 1 2 3 (+ 2 2))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(1 2 3 4)");
            }
            {
                String answer = ts.parse(
                        "(if 1 2)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "2");
            }
            {
                String answer = ts.parse(
                        "(if (= 3 4) 2)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(define ((account bal) amt) (set! bal (+ bal amt)) bal)"
//                        "(define ((account bal) amt) (begin (set! bal (+ bal amt)) bal))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(define a1 (account 100))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(a1 0)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "100");
            }
            {
                String answer = ts.parse(
                        "(a1 10)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "110");
            }
            {
                String answer = ts.parse(
                        "(a1 10)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "120");
            }
            {
                String answer = ts.parse(
                        "(define (newton guess function derivative epsilon)"+
                            "(define guess2 (- guess (/ (function guess) (derivative guess))))"+
                            "(if (< (abs (- guess guess2)) epsilon) guess2"+
                                "(newton guess2 function derivative epsilon)))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(define (square-root a)"+
                            "(newton 1 (lambda (x) (- (* x x) a)) (lambda (x) (* 2 x)) 1e-8))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(> (square-root 200.) 14.14213)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse(
                        "(< (square-root 200.) 14.14215)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse(
                        "(< (square-root 200.) 14.14215)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse(
                        "(< (square-root 200.) 14.14215)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse(
                        "(= (square-root 200.) (sqrt 200.))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse(
                        "(define (sum-squares-range start end)"+
                            "(define (sumsq-acc start end acc)"+
                                "(if (> start end) acc (sumsq-acc (+ start 1) end (+ (* start start) acc))))"+
                            "(sumsq-acc start end 0))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(sum-squares-range 1 3000)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "9004500500");
            }
            {
                String answer = ts.parse(
                        "(call/cc (lambda (throw) (+ 5 (* 10 (throw 1)))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "1");
            }
            {
                String answer = ts.parse(
                        "(call/cc (lambda (throw) (+ 5 (* 10 1))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "15");
            }
            {
                String answer = ts.parse(
                        "(call/cc (lambda (throw)"+
                            "(+ 5 (* 10 (call/cc (lambda (escape) (* 100 (escape 3))))))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "35");
            }

            {
                String answer = ts.parse(
                        "(call/cc (lambda (throw)"+
                            "(+ 5 (* 10 (call/cc (lambda (escape) (* 100 (throw 3))))))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
            {
                String answer = ts.parse(
                        "(call/cc (lambda (throw)"+
                            "(+ 5 (* 10 (call/cc (lambda (escape) (* 100 1)))))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "1005");
            }
// 複素数は未対応
//            {
//                String answer = ts.parse(
//                        "(* 1i 1i)"
//                );
//                System.out.println("answer:[" + answer + "]");
//                assertEquals(answer, "(-1+0i)");
//            }
//            {
//                String answer = ts.parse(
//                        "(sqrt -1)"
//                );
//                System.out.println("answer:[" + answer + "]");
//                assertEquals(answer, "1i");
//            }
            {
                String answer = ts.parse(
                        "(let ((a 1) (b 2)) (+ a b))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
            {
                String answer = ts.parse(
                        "(and 1 2 3)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
            {
                String answer = ts.parse(
                        "(and (> 2 1) 2 3)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "3");
            }
            {
                String answer = ts.parse(
                        "(and)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#t");
            }
            {
                String answer = ts.parse(
                        "(and (> 2 1) (> 2 3))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "#f");
            }
            {
                String answer = ts.parse(
                        "(define-macro unless (lambda args `(if (not ,(car args)) (begin ,@(cdr args)))))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(unless (= 2 (+ 1 1)) (display 2) 3 4)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "(unless (= 4 (+ 1 1)) (display 2) (display \"\\n\") 3 4)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "4");
            }
            {
                String answer = ts.parse(
                        "(quote x)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "x");
            }
            {
                String answer = ts.parse(
                        "(quote (1 2 three))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(1 2 three)");
            }
            {
                String answer = ts.parse(
                        "'x"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "x");
            }
            {
                String answer = ts.parse(
                        "'(one 2 3)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(one 2 3)");
            }
            {
                String answer = ts.parse(
                        "(define L (list 1 2 3))"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "");
            }
            {
                String answer = ts.parse(
                        "`(testing ,@L testing)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(testing 1 2 3 testing)");
            }
            {
                String answer = ts.parse(
                        "`(testing ,L testing)"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(testing (1 2 3) testing)");
            }
            {
                // コメントは改行まで
                String answer = ts.parse(
                        "'(1 ;test comments '\r\n" +
                        "     ;skip this line\r\n" +
                        "     2 ; more ; comments ; ) )\r\n" +
                        "     3) ; final comment\r\n"
                );
                System.out.println("answer:[" + answer + "]");
                assertEquals(answer, "(1 2 3)");
            }

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

}
