import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import static java.lang.Double.parseDouble;
import static java.lang.Math.min;
import static java.lang.String.valueOf;

public class jikken extends JFrame {//クラス
    //Main以外でも使うからここで宣言
    JPanel contentPane = new JPanel(new GridBagLayout());//パネルの作成
    JPanel textPanel = new JPanel();
    JTextField result = new JTextField("0", 38);//計算結果を示すテキスト領域
    JMenuBar menubar = new JMenuBar();
    GridBagConstraints gbc = new GridBagConstraints();

    //演算子ボタンを押す前にテキスト領域に表示されている数値
    boolean afterCalc = false;
    boolean isAfterCalc = false;
    String calcOp = "";
    String calcOp2 = "";
    BigDecimal resultValue = BigDecimal.ZERO;
    BigDecimal value = BigDecimal.ZERO;
    BigDecimal NEGATIVE_plMAX = new BigDecimal("-1E9999");//上限下限
    BigDecimal POSITIVE_plMAX = new BigDecimal("1E9999");
    BigDecimal NEGATIVE_miMAX = new BigDecimal("-1E-9999");//小数点の上限下限
    BigDecimal POSITIVE_miMAX = new BigDecimal("1E-9999");
    long resultLongValue = 0;
    long Longvalue = 0;
    CheckboxGroup cbox = new CheckboxGroup();
    JCheckBox radio16; //16進数ボタン
    JCheckBox radio10;
    JButton dotto_button = new NumberButton(".");
    JButton hex_button[] = new JButton[6];


    //コンストラクタ
    public jikken() {/*
        メソッド、関数
        Mainの構成要素
        */

        JPanel panel = new JPanel();//数字用と演算子用のパネル作成
        JPanel equalPanel = new JPanel();

        BorderLayout layout1 = new BorderLayout();//新しいボーダレイアウトの構成
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ｘボタンで終了
        setTitle("電卓");//タイトル
        setSize(400, 400);//縦横比
        setResizable(false);
        setLocationRelativeTo(null);//nullで真ん中表示
        setContentPane(contentPane);
        contentPane.setLayout(layout1);
        //contentPaneをフレームのパネルとして設定
        radio16 = new JCheckBox("16進数", false, cbox);//16進数ボタン
        radio10 = new JCheckBox("10進数", true, cbox);//10進数ボタン
        menubar.setLayout(new FlowLayout(FlowLayout.LEFT));//左寄せ
        menubar.add(radio10);//メニューバーに追加
        menubar.add(radio16);//同上
        setJMenuBar(menubar);//画面にセット
        //テキスト領域を作成
        textPanel.setLayout(new GridBagLayout());

        gbc.weightx = 0.5;
        gbc.weighty = 1;   // 高さ
        gbc.fill = GridBagConstraints.BOTH;   // ギャップ(幅)を詰める

        gbc.gridx = 0;//テキストパネル
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;//最後
        gbc.fill = GridBagConstraints.HORIZONTAL;
        result.setFont(new Font("", Font.PLAIN, 20));
        textPanel.add(result, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        CalcButton i = new CalcButton("C");
        textPanel.add(i, gbc);
        i.setFont(new Font("", Font.PLAIN, 15));
        i.setBackground(new Color(250, 120, 120));


        int[] grid_x = {0, 1, 2, 3, 4, 5};
        int[] grid_y = {2, 2, 2, 2, 2, 2};
        String[] buttons = {"A", "B", "C", "D", "E", "F"};
        int height;
        for (int f = 0; f < 6; f++) {
            JButton button = new NumberButton(buttons[f]);
            button.setFont(new Font("", Font.PLAIN, 18));
            height = button.getMaximumSize().height;
            button.setMaximumSize(new Dimension(Short.MAX_VALUE, height));
            gbc.gridwidth = 1;
            gbc.gridx = grid_x[f];
            gbc.gridy = grid_y[f];
            textPanel.add(button, gbc);
            hex_button[f] = button;
            button.setEnabled(false);
            button.setBackground(new Color(236, 236, 236));
        }


        contentPane.add(textPanel, BorderLayout.NORTH);//contentPane内の北に配置
        result.setHorizontalAlignment(JTextField.RIGHT);
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
        panel.add(dotto_button);
        panel.add(new CalcButton("＋"));

        equalPanel.setLayout(new GridLayout(1, 1));
        contentPane.add(equalPanel, BorderLayout.SOUTH);
        equalPanel.add(new CalcButton("＝")).setFont(new Font("", Font.PLAIN, 15));

        //下に配置
        setVisible(true);//表示・非表示
    }


    public class JCheckBox extends Checkbox implements ItemListener {
        public JCheckBox(String label, boolean state, CheckboxGroup group) throws HeadlessException {
            super(label, state, group);
            addItemListener(this);
        }


        public void itemStateChanged(ItemEvent e) {
            if (radio16.getState()) {//16進数だったら

                try {
                    POSITIVE_plMAX = new BigDecimal("9223372036854775807");//限界値をPOSITIVE_MAX9223372036854775807にする

                    BigDecimal result_dotto ;
                    if (isAfterCalc) {
                        value = new BigDecimal(result.getText());

                        result_dotto = value.setScale(0,BigDecimal.ROUND_DOWN);
                    } else {
                        resultValue = new BigDecimal(result.getText());
                        resultValue = resultValue.setScale(0, BigDecimal.ROUND_DOWN);//小数点以下切捨て
                        result_dotto = new BigDecimal(resultValue.toPlainString());
                    }
                    if (result_dotto.compareTo(POSITIVE_plMAX) < 0) {
                        long dec = Long.parseLong(result_dotto.toPlainString());
                        String resultValue2 = Long.toHexString(dec);
                        result.setText(resultValue2.toUpperCase());
                    } else {
                        result.setText("値が大きすぎます");
                        resultValue = BigDecimal.ZERO;
                    }
                    dotto_button.setBackground(new Color(236, 236, 236));//ボタン灰色
                    for (JButton button : hex_button) {//ボタンを有効
                        button.setEnabled(true);
                        button.setBackground(new JButton().getBackground());//ボタン元の色
                    }
                } catch (NumberFormatException li) {
                    result.setText("値が大きすぎます");
                    resultValue = BigDecimal.ZERO;
                }
            } else {
                POSITIVE_plMAX = new BigDecimal("1E9999");//最大値を元に戻す
                try {
                    long resultValue2;
                    if (isAfterCalc) {
                        resultValue2 = Long.parseLong(result.getText(), 16);
                        value = BigDecimal.valueOf(resultValue2);
                        value = value.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN);
                        result.setText(value.toPlainString());
                    } else {
                        resultValue2 = Long.parseLong(result.getText(), 16);
                        resultValue = BigDecimal.valueOf(resultValue2);
                        resultValue = resultValue.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN);
                        result.setText(resultValue.toPlainString());
                    }
                } catch (NumberFormatException La) {
                    resultValue = resultValue.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN);
                    result.setText(resultValue.toPlainString());
                }
                dotto_button.setBackground(new JButton().getBackground());//ボタン元の色
                for (JButton button : hex_button) {//A～Fボタン
                    button.setEnabled(false);
                    button.setBackground(new Color(236, 236, 236));
                }
            }
        }
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


            if (afterCalc || result.getText().equals("0") || result.getText().equals("00")) {//0と00の時

                if (this.getText().equals(".")) {//小数点ボタン押したら
                    if (!radio16.getState()) {//小数点以外なら
                        result.setText("0.");
                    }
                } else {
                    result.setText(this.getText());
                }
                afterCalc = false;
            } else {

                int dem = 0;
                if (result.getText().contains("0.")) {//0.を入力したら
                    dem += 2;
                } else if (result.getText().contains(".")) {//.を入力したら
                    dem += 1;
                } else if (radio16.getState()) {//f16個で0になるため
                    dem -= 1;
                }
                //小数点は1つまで、文字数は16文字まで(最初の0.や.は数に含まない)
                if ((result.getText().contains(".") || radio16.getState())
                        && this.getText().equals(".")
                        || result.getText().length() >= 16 + dem) {
                } else {
                    result.setText(result.getText() + this.getText());//連結1と0押して10
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
                //初期化
                result.setText("0");
                resultValue = BigDecimal.valueOf(0);
                value = BigDecimal.valueOf(0);
                calcOp = ("");
                afterCalc = false;
                isAfterCalc = false;
                //演算子ボタンが押されたら

            } else if (this.getText().equals("＋") || this.getText().equals("－")
                    || this.getText().equals("×") || this.getText().equals("÷")
                    || this.getText().equals("＝")) {
                //＝が入っていて+-x÷押したら格納していた演算子をいったん削除
                if (calcOp2.equals("＝")) {
                    if (!this.getText().equals("＝")) {
                        calcOp = "";
                    }
                }
                //演算子が格納されている？
                if (calcOp.equals("＋") || calcOp.equals("－") || calcOp.equals("×") ||
                        calcOp.equals("÷")) {

                    if (!calcOp2.equals("＝")) {
                        try {
                            //演算子入力後のテキスト領域の文字を代入
                            if (radio16.getState()) {
                                Longvalue = Long.parseLong(result.getText(), 16);
                                value = new BigDecimal(Longvalue);
                            } else {
                                value = new BigDecimal(result.getText());
                            }
                        } catch (NumberFormatException d) {
                            value = BigDecimal.valueOf(0);
                        }
                    }
                    switch (calcOp) {
                        case "÷": //格納された演算子が÷だったら
                            if (radio16.getState()) {
                                try {
                                    resultValue = resultValue.divide
                                            (value, 0, BigDecimal.ROUND_DOWN);//割り算を行い、100桁目を切り捨て
                                } catch (ArithmeticException f) {
                                    resultValue = BigDecimal.ZERO;
                                }
                            } else {
                                try {
                                    resultValue = resultValue.divide
                                            (value, 100, BigDecimal.ROUND_DOWN);//割り算を行い、100桁目を切り捨て
                                } catch (ArithmeticException d) {
                                    resultValue = BigDecimal.valueOf(0);
                                }
                            }
                            break;
                        case "×":
                            resultValue = resultValue.multiply(value);
                            break;
                        case "－":
                            resultValue = resultValue.subtract(value);
                            break;
                        case "＋":
                            resultValue = resultValue.add(value);
                            break;
                    }
                    if (calcOp.equals("÷") && (parseDouble(valueOf(value)) == 0)) {
                        //押された演算子が÷で演算子入力後に代入された値が0だったら
                        result.setText("0で割ることはできません");
                        resultValue = BigDecimal.valueOf(0);
                    } else if (resultValue.compareTo(POSITIVE_plMAX) >= 0
                            || resultValue.compareTo(NEGATIVE_plMAX) <= 0) {//上限値、下限値を超えたら
                        result.setText("値が大きすぎます");
                        resultValue = BigDecimal.valueOf(0);


                    } else if ((resultValue.compareTo(BigDecimal.ZERO) > 0
                            && resultValue.compareTo(POSITIVE_miMAX) <= 0)//0超えで上限桁数以下
                            || (resultValue.compareTo(BigDecimal.ZERO) < 0
                            && resultValue.compareTo(NEGATIVE_miMAX) >= 0)) {//0未満で上限桁数以下
                        result.setText("値が小さすぎます");
                        resultValue = BigDecimal.valueOf(0);
                    } else {
                        BigDecimal resultValue2;
                        //表示用の変数に代入し、16桁-正数で最終桁を四捨五入
                        resultValue2 = resultValue.setScale
                                (16 - (resultValue.precision() - resultValue.scale()),
                                        BigDecimal.ROUND_HALF_EVEN);
                        //文字列の一番右の値が0もしくは結果が0だったら一番右の0を取る
                        if (resultValue2.scale() > 0 || resultValue2.compareTo(BigDecimal.ZERO) == 0) {
                            String resultStr = resultValue2.toPlainString();
                            resultStr = resultStr.substring(0, min(10000, resultStr.length()));
                            try {
                                while (resultStr.substring(resultStr.length() - 1).equals("0")) {
                                    resultStr = resultStr.substring(0, resultStr.length() - 1);
                                }
                            } catch (StringIndexOutOfBoundsException f) {
                                resultValue = BigDecimal.ZERO;
                                resultStr = "0";
                            }
                            resultValue2 = new BigDecimal(resultStr);
                        }
                        //resultStr2に全てを数字表記にして代入
                        //正数で0.0001超えand99~9未満or負数で-0.001未満and-99~9超え
                        if (radio16.getState()) {
                            resultValue = resultValue.setScale(0, BigDecimal.ROUND_DOWN);
                            long dec = Long.parseLong(resultValue.toPlainString());
                            String resultValue3 = Long.toHexString(dec);
                            result.setText(resultValue3.toUpperCase());
                        } else if ((resultValue2.compareTo(BigDecimal.valueOf(0.001)) > 0
                                && resultValue2.compareTo(BigDecimal.valueOf(9999999999999999d)) < 0)
                                || (resultValue2.compareTo(BigDecimal.valueOf(-0.001)) < 0
                                && resultValue2.compareTo(BigDecimal.valueOf(-9999999999999999d)) > 0)
                                || resultValue2.equals(BigDecimal.ZERO)) {
                            result.setText(resultValue2.toPlainString());
                        } else {
                            //指数表記にして表示
                            DecimalFormat format1 = new DecimalFormat("#.###############E0");
                            result.setText(format1.format(resultValue2));
                        }
                    }
                } else {//格納されていなかったら
                        if (radio16.getState()) {
                            resultLongValue = Long.valueOf(result.getText(), 16);
                            resultValue = new BigDecimal(resultLongValue);
                        } else {
                            resultValue = new BigDecimal(result.getText());

                        }
                }
                if (!this.getText().equals("＝")) {
                    calcOp = this.getText();//押された演算子を代入
                }
            }
            calcOp2 = this.getText();
            afterCalc = true;//演算子ボタンを押したらtrue
            isAfterCalc = true;
        }
    }

    public static void main(String[] args) {
        new jikken();//Javaアプリケーションでの起動
    }
}