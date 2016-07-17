# TinyScheme
Lispの方言のSchemeのJavaによる学習用簡易実装です。
過去にあった同様のものに特に勝っている要素はないと思います。（下記サイトにもJava実装があるようです）
基本的に下記サイトのlispy.pyの移植となっています。  
日本語翻訳<http://www.aoky.net/articles/peter_norvig/lispy2.htm>  
英語オリジナル<http://norvig.com/lispy2.html>  

##実行方法
対話モード  
java -jar jar/Scheme.jar  
ファイル読み込み  
java -jar jar/Scheme.jar ファイル名  
例）  
java -jar jar/Scheme.jar sample.txt  

##対話モードの操作
(exit)  
で終了します。

##言語仕様
最低限程度の機能しかありません。
詳細の文法や対応している機能や関数、制約などはテストコード(SchemaTest.java)を参照してください。
