import javax.print.attribute.HashAttributeSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import static java.lang.Math.min;
import static java.lang.String.*;
import static java.lang.String.valueOf;

public class Dentaku extends JFrame {//クラス
    //Main以外でも使うからここで宣言

    JPanel contentPane = new JPanel();//パネルの作成
    JTextField result = new JTextField("0", 36);//計算結果を示すテキスト領域

    //演算子ボタンを押す前にテキスト領域に表示されている数値
    boolean afterCalc = false;
    String calcOp = "";
    double resultValue;

おしるこ
    //コンストラクタ
    public Dentaku() {/*
        メソッド、関数
        Mainの構成要素
        */
        JPanel panel = new JPanel();//数字用と演算子用のパネル作成
        JPanel textPanel = new JPanel();//テキスト領域用のパネル作成

        BorderLayout layout1 = new BorderLayout();//新しいボーダレイアウトの構成
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ｘボタンで終了
        setTitle("電卓");//タイトル
        setSize(300, 300);//縦横比
        setLocationRelativeTo(null);//nullで真ん中表示
        setContentPane(contentPane);
        contentPane.setLayout(layout1);

        //contentPaneをフレームのパネルとして設定
        //テキスト領域を作成
        textPanel.setLayout(new GridLayout(2, 0));//縦列
        textPanel.add(result);
        textPanel.add(new CalcButton("C")).setBackground(new Color(250, 120, 120));
        contentPane.add(textPanel, BorderLayout.NORTH);//contentPane内の北に配置

        result.setHorizontalAlignment(JTextField.RIGHT);

        //数字ボタンのパネル作成
        contentPane.add(panel, BorderLayout.CENTER);//contentPane内の真ん中に設置
        panel.setLayout(new GridLayout(4, 4));//4行4列の分割

        //ボタンを左上から上から順に設置
        //数字ボタン
        panel.add(new NumberButton("7"));
        panel.add(new NumberButton("8"));
        panel.add(new NumberButton("9"));
        panel.add(new CalcButton("÷"));
        panel.add(new NumberButton("4"));
        panel.add(new NumberButton("5"));
        panel.add(new NumberButton("6"));
        panel.add(new CalcButton("×"));
        panel.add(new NumberButton("1"));
        panel.add(new NumberButton("2"));
        panel.add(new NumberButton("3"));
        panel.add(new CalcButton("－"));
        panel.add(new NumberButton("0"));
        panel.add(new NumberButton("00"));
        panel.add(new NumberButton("."));
        panel.add(new CalcButton("＋"));

        contentPane.add(new CalcButton("＝"), BorderLayout.SOUTH);//下に配置
        setVisible(true);//表示・非表示
    }

    //数字ボタンの定義
    public class NumberButton extends JButton implements ActionListener {//JButtonの継承とアクション設定
        //implementsはメソッドに追加、定義とほぼ同意、複数のインターフェースを実装できるActionListenerはインターフェース

        NumberButton(String numberKey) {
            super(numberKey);//親クラスのコンストラクタJButtonを呼び出す
            addActionListener(this);//数字ボタンにイベント設定
            //押した数字がActionListenerObject(thisで自分自身)
        }

        //アクションが起きた場合（今回はボタンが押されたとき）
        public void actionPerformed(ActionEvent e) {

            if (afterCalc || result.getText().equals("0") || result.getText().equals("00")) {
                //演算子が押された後、またはテキスト領域が｢0｣、またはテキスト領域が｢00｣の時
                if (this.getText().equals(".")) {
                    //押したボタンが｢.｣の時
                    result.setText("0.");
                } else {
                    //｢.｣じゃないとき
                    result.setText(this.getText());
                }
                afterCalc = false;
            } else {
                if (result.getText().contains(".") && this.getText().equals(".") || result.getText().length() >= 15) {
                    //テキスト領域に｢.｣があるとき、それに加えて押したボタンが｢.｣もしくはテキスト領域の文字数が15文字になっていた時
                } else {
                    result.setText(result.getText() + this.getText());
                }
            }
        }
    }

    //演算子ボタンで利用する変数の定義
//インスタンスが5個あるため、メソッド内にまとめられない
    public class CalcButton extends JButton implements ActionListener {

        CalcButton(String calcKey) {
            super(calcKey);
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {

            //クリアボタンの設定
            if (this.getText().equals("C")) {
                //｢C｣を押した時
                result.setText("0");
                resultValue = 0;
                calcOp = ("");
                afterCalc = false;

                //演算子ボタンが押されたら
            } else if (this.getText().equals("＋") || this.getText().equals("－")
                    || this.getText().equals("×") || this.getText().equals("÷")
                    || this.getText().equals("＝")) {

                //演算子が格納されている？
                if (calcOp.equals("＋") || calcOp.equals("－") || calcOp.equals("×") ||
                        calcOp.equals("÷")) {
                    double value;
                    double NEGATIVE_MAX = -100000000000000000d;
                    double POSITIVE_MAX = 10000000000000000d;

                    value = Double.valueOf(result.getText());

                    if (calcOp.equals("÷")) {
                        try {
                            resultValue *= 100;
                            resultValue /= value;
                            resultValue /= 100;
                        } catch (ArithmeticException d) {
                            resultValue = 0;
                        }
                    } else if (calcOp.equals("×")) {
                        resultValue *= 100;
                        value *= 100;
                        resultValue *= value;
                        resultValue /= 10000;
                    } else if (calcOp.equals("－")) {
                        resultValue *= 100;
                        value *= 100;
                        resultValue -= value;
                        resultValue /= 100;
                    } else if (calcOp.equals("＋")) {
                        resultValue *= 100;
                        value *= 100;
                        resultValue += value;
                        resultValue /= 100;
                    }


                    if (calcOp.equals("÷") && (Double.valueOf(valueOf(value)) == (0))) {
                        result.setText("0で割ることはできません");
                        resultValue = 0;
                    } else if (resultValue >= POSITIVE_MAX || resultValue <= NEGATIVE_MAX) {
                        result.setText("値が大きすぎます");
                        resultValue = 0;
                    } else {
                        BigDecimal ResultValue = new BigDecimal(resultValue);
                        String s = BigDecimal.valueOf(Double.parseDouble(String.valueOf(ResultValue))).toPlainString();
                        String digits = s.substring(0, min(100, s.length()));
                        while (digits.substring(digits.length() - 1).equals("0")||digits.substring(digits.length() - 1).equals(".")) {
                            digits = digits.substring(0, digits.length() - 1);
                        }
                        result.setText(digits);
                    }
                } else {
                    //格納されていなかったら
                    try {
                        resultValue = Double.valueOf(result.getText());
                    } catch (NumberFormatException d) {
                        resultValue = 0;
                    }
                }
            }

            calcOp = this.getText();
            afterCalc = true;
        }

    }

    public static void main(String[] args) {
        new Dentaku();//Javaアプリケーションでの起動
        //public どのクラスからでもアクセスできる
        //static クラスに属するメソッドの修飾子　インスタンス化（＝new）せずに使える
        //void 戻り値なし
        //main 最初に呼び出されるメソッド、アプリケーション開始メソッド
    }
}//kokekokeko