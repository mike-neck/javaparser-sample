javaparser-sample
===

単なる `javaparser` のサンプル。
読み込んだJavaファイルを指定のフォーマットに整形して好みの
`Writer`(あるいは `StringBuilder`)に出力する。

Usage
---

こんな感じ。

```java
public class Runner {
  public static void main(String[] args){
    final FormatConfig config = FormatConfig.builder()
        .indent(IndentStyle.space(4))
        .newLine(NewLine.SYSTEM)
        .with(ParameterStyle.newLineOnEach().commaOnEnd().indent(2))
        .build();
    final CompilationUnit cu = JavaParser.parse(new File("MyJavaFile.java"));
    final StringBuilder sb = new StringBuilder();
    ConfigurablePrinter.prepare(config)
        .print(cu).to(sb);
    System.out.println(sb.toString());
  }
}
```

Install
---

Mavenへのリリースは多分ないので、このリポジトリをクローンして適当に使う。
