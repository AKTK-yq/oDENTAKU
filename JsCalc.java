import javax.script.*;
import java.util.Scanner;

class JsCalc {                                        // JsCalc.java [java]
    static ScriptEngineManager factory = new ScriptEngineManager();
    static ScriptEngine engine = factory.getEngineByName("JavaScript");

    // JavaでJavaScriptのeval()関数を呼び出す
    static String sEval(String sExpr) {
        String sScript="with(Math){"+sExpr+"}";       // 「Math.」を省略可能にするため
        try{
            return( engine.eval(sScript).toString() );
        }
        catch(NullPointerException e){               // 関数定義でエラー防止
            return(sExpr);
        }
        catch(Exception e){
            return("error: "+e);
        }
    }

    public static void main(String[] args) {
        Scanner stdIn = new Scanner(System.in);

        defUserFunc();                               // ユーザー定義関数の設定
        System.out.println("簡易関数電卓 JsCalc");
        for(;;){                                     // 無限ループ
            System.out.print("> ");
            String sExpr = stdIn.nextLine();         // １行分の文字列を読み込む
            if(sExpr.equalsIgnoreCase("@Q")) break;  // 無限ループの出口:「@Q」で終了
            System.out.println(" "+sEval(sExpr));    // 表示
        }
    }

    // ユーザー定義関数の設定
    static void defUserFunc() {
        String sScript="";
        sEval("function radians(x){return(x/180*PI);}");
        sEval("function degrees(x){return(x/PI*180);}");
        sEval("function sinDeg(x) {return(sin(radians(x)));}");
        sEval("function cosDeg(x) {return(cos(radians(x)));}");
        sEval("function tanDeg(x) {return(tan(radians(x)));}");
        // 長文用
        sScript = "function sum(a){var s=0;"
                + "for(var i=0; i< a.length; i++) s+=a[i];"
                + "return(s);}";
        sEval(sScript);
        // 後はご自由に追加してください。

    }
}