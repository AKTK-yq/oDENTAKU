import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

public class JsCalcW3 {                         // JsCalcW.java [java]
    private static void createAndShowGUI() {
        // フレームの作成
        JFrame frame = new JFrame("JsCalcW");   // JFrameオブジェクトを生成
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // ウィンドウを閉じるとプログラムが終了
        MyPanel h = new MyPanel();
        frame.add(h, BorderLayout.CENTER);      // フレームにオブジェクトを置く
        frame.pack();                           // フレームを必要最小の大きさにする
        frame.setMinimumSize(new Dimension(frame.getSize().width, frame.getSize().height));
        // 最小サイズを指定 1.6以上で有効
        frame.setAlwaysOnTop(true);             // 最前面ウィンドウへ設定
        frame.setVisible(true);                 // フレームを表示する
    }

    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

class MyPanel extends JPanel implements ActionListener { // JPanelを継承
    JButton    btn1, btn2;
    JTextField txt1, txt2;
    ArrayList<String> sHis = new ArrayList<String>();
    int iCnt = 0;

    // 追加ボタンの設定
    String [] sBtD = {                          // 表示用
            "arc","hyp","sin","cos","tan",
            "x^y","，","exp","ln","log",
            "10^x","1/x","3√","π","ｅ",

            "７","８","９","÷","CE",
            "４","５","６","×","√",
            "１","２","３","−","％",
            "０","．","（","＋","）"
    };
    String [] sBtO = {                          // 出力用
            "a","h(","sin","cos","tan",
            "pow(",",","exp(","ln(","log10(",
            "ten(","rcp(","cbrt(","PI","E",

            "7","8","9","/","",
            "4","5","6","*","sqrt(",
            "1","2","3","-","%",
            "0",".","(","+",")"
    };
    int nBtn = sBtD.length;                     // 追加するボタンの数
    JButton[] aBtn = new JButton[nBtn];         // 追加ボタン

    public MyPanel() {
        setBackground(SystemColor.control);     // 背景を灰色にする

        txt1 = new JTextField(14);              // txtの設定
        txt1.addActionListener(this);
        txt1.addKeyListener(new MyKeyListener());   // リスナーオブジェクトを指定
        txt1.setFont(new Font("SansSerif",Font.PLAIN,16));

        txt2 = new JTextField(14);              // txtの設定
        txt2.setFont(new Font("SansSerif",Font.ITALIC,16));
        txt2.setHorizontalAlignment(JTextField.RIGHT);

        btn1 = new JButton("＝");
        btn1.addActionListener(this);

        btn2 = new JButton("Ｃ");
        btn2.addActionListener(this);

        for(int i = 0; i< nBtn; i++)
            aBtn[i] = new JButton(sBtD[i]);
        for(JButton b : aBtn)
            b.addActionListener(this);

        JPanel pa = new JPanel();
        pa.setLayout(new GridLayout(2,1,2,2));
        pa.add(txt1);
        pa.add(txt2);

        JPanel pb = new JPanel();
        pb.setLayout(new GridLayout(2,1,2,2));
        pb.add(btn1);
        pb.add(btn2);

        JPanel pc = new JPanel();
        pc.setLayout(new GridLayout(nBtn/5,5,2,2));
        for(JButton b : aBtn)
            pc.add(b);

        setLayout(new BorderLayout(2,2));
        add(pa, BorderLayout.CENTER);
        add(pb, BorderLayout.EAST);
        add(pc, BorderLayout.SOUTH);

        sHis.add(""); iCnt = sHis.size()-1;
        MyJsCalc.defUserFunc();                 // ユーザー定義関数の設定
    }

    public void actionPerformed(ActionEvent e) {    // ボタンクリック
        if(e.getSource()==btn1||e.getSource()==txt1) {
            String s = txt1.getText(); sHis.add(s); iCnt = sHis.size()-1;
            txt2.setText(MyJsCalc.sEval(s));
        } else if(e.getSource()==btn2) {
            txt1.setText(""); txt2.setText("");
        } else if(e.getSource()==aBtn[Arrays.asList(sBtD).indexOf("CE")]) {
            String s = txt1.getText();          // [CE]ボタンの処理
            if(s.length()> 0) s = s.substring(0, s.length()-1);
            txt1.setText(s);
        }
        for(int i = 0; i< nBtn; i++)
            if(e.getSource()==aBtn[i])
                txt1.setText(txt1.getText()+sBtO[i]);
        txt1.requestFocus();
    }

    class MyKeyListener extends KeyAdapter {    // リスナークラス
        public void keyPressed(KeyEvent e){
            int k = e.getKeyCode();

            if       (k == KeyEvent.VK_DOWN) {
                iCnt++;
                if(iCnt> sHis.size()) iCnt = sHis.size();
                txt2.setText(""); txt1.setText(sHis.get(iCnt % sHis.size()));
            } else if(k == KeyEvent.VK_UP) {
                iCnt--;
                if(iCnt< 1) iCnt = 1;
                txt2.setText(""); txt1.setText(sHis.get(iCnt % sHis.size()));
            }
        }
    }
}

class MyJsCalc extends JsCalc {
    // ユーザー定義関数の設定
    static void defUserFunc() {
        JsCalc.defUserFunc();   // staticメソッドはオーバーライドできない
        // 関数を追加
        String sScript = "";
        sScript = "sign = function(x) {x=+x;"
                + "if(x===0||isNaN(x)){return x;}"
                + "return (x> 0)?1:-1;}";
        sEval(sScript);
        sEval("cbrt  = function(x) {return sign(x)*pow(abs(x),1/3);}");
        sEval("rcp   = function(x) {return 1/x;}");
        sEval("Rnd   = function()  {return random();}");
        sEval("sinh  = function(x) {return (exp(x)-exp(-x))/2;}");
        sEval("sinh  = function(x) {return (exp(x)-exp(-x))/2;}");
        sEval("cosh  = function(x) {return (exp(x)+exp(-x))/2;}");
        sScript = "tanh = function(x) {"
                + "if(abs(x)===Infinity){return sign(x);}"
                + "else{var y=exp(2*x);return (y-1)/(y+1);}}";
        sEval(sScript);
        sEval("asinh = function(x) {return(x===-Infinity)?x:log(x+sqrt(x*x+1));}");
        sEval("acosh = function(x) {return log(x+sqrt(x*x-1));}");
        sEval("atanh = function(x) {return log((1+x)/(1-x))/2;}");
        sEval("ln    = function(x) {return log(x);}");
        sEval("log10 = function(x) {return log(x)/LN10;}");
        sEval("ten   = function(x) {return pow(10,x);}");
        // 後はご自由に追加してください。 sEval("");
    }
}