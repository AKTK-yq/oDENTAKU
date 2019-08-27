import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Dentaku2 {
    Frame f0;
    Label dsp;
    Font fbig,fmid;
    Button btn[]=new Button[20];
    Checkbox hx,dc;
    CheckboxGroup cbg;

    final int MAXV=0x3FFFFFFF;
    final int MINV=-MAXV;
    final int NOP=0, ADD=1, SUB=2, NEXT=3;
    int result=0,val1=0;
    int radix=10;
    int stat=NOP;
    boolean overflow=false;

    final int KCLR=16, KEQU=17,KPLS=18,KMNS=19;

    String lb[]={
            "0","1","2","3",
            "4","5","6","7",
            "8","9","A","B",
            "C","D","E","F",
            "CLR","=","+","-"
    };

    int pos[]={
            10,11,12,13,
            7, 8, 9,14,
            4, 5, 6,15,
            1, 2, 3,KCLR,
            0,KPLS,KMNS,KEQU
    };

    Dentaku2(){
        f0=new Frame();
        f0.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
        f0.setTitle("decHEX5");
        fbig=new Font("Monospaced",Font.PLAIN,36);
        fmid=new Font("Monospaced",Font.PLAIN,24);

        dsp=new Label("0");
        dsp.setAlignment(Label.RIGHT);
        dsp.setFont(fbig);
        dsp.setBackground(Color.LIGHT_GRAY);

        Panel p1=new Panel();
        p1.setLayout(new BorderLayout());

        cbg=new CheckboxGroup();
        dc=new Checkbox("dec",true,cbg);
        hx=new Checkbox("HEX",false,cbg);
        Panel prd=new Panel();
        prd.setLayout(new FlowLayout(FlowLayout.LEFT));
        prd.add(dc); prd.add(hx);
        p1.add(prd,BorderLayout.SOUTH);
        p1.add(dsp,BorderLayout.CENTER);

        Panel keys=new Panel();
        keys.setLayout(new GridLayout(5,4));

        for (int i=0;i<20;++i) { // btn[]をセット

            if (i>15) btn[i]=new Button(lb[i]);
            else btn[i]=new nBtn(lb[i],i);

            switch (i) {
                case KCLR:
                    btn[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            result=val1=0;
                            overflow=false;
                            display();
                        }
                    });
                    break;
                case KPLS:
                    btn[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!overflow) {
                                val1=calc();
                                stat=ADD;
                                result=0;
                                display("+");
                            }
                        }
                    });
                    break;
                case KMNS:
                    btn[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!overflow) {
                                val1=calc();
                                stat=SUB;
                                result=0;
                                display("-");
                            }
                        }
                    });
                    break;
                case KEQU:
                    btn[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!overflow) {
                                result=calc();
                                stat=NEXT;
                            }
                            display();
                        }
                    });
                    break;
                default:
                    break;
            }
            btn[i].setFont(fmid);

        }
        for (int i=0;i<20;++i) keys.add(btn[pos[i]]); //パネルに格納

        hx.addItemListener(new setRadix());
        dc.addItemListener(new setRadix());

        hkeytop();
        f0.add(p1,BorderLayout.NORTH);
        f0.add(keys,BorderLayout.CENTER);
        f0.setSize(300,300);
        f0.setVisible(true);
    }
    public static void main(String[] args) {
        new Dentaku2();
    }
    void display(){
        String rs;
        if (overflow) {
            rs="Overflow";
        }else{
            if (radix==10)	rs=Integer.toString(result);
            else rs=Integer.toHexString(result).toUpperCase();
        }
        dsp.setText(rs);
    }
    void display(String s){
        if (overflow) s="Overflow";
        dsp.setText(s);
    }
    void hkeytop(){
        for (int k=10;k<16;++k){
            if (radix==16) btn[k].setForeground(Color.BLACK);
            else btn[k].setForeground(Color.LIGHT_GRAY);
        }
    }

    int calc(){
        int rv=result;
        if (!overflow) {
            if (stat==ADD) {
                rv=val1+result;
                if (rv>MAXV) overflow=true;
            }else if (stat==SUB) {
                rv=val1-result;
                if (rv<MINV) overflow=true;
            }
        }
        return rv;
    }
    class nBtn extends Button implements ActionListener {
        private static final long serialVersionUID = 1L;
        int nn;
        nBtn(String lb,int v){
            super(lb);
            nn=v;
            this.addActionListener(this);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!overflow) {
                if ((radix==16)||(nn<10)) {
                    if (stat==NEXT) {
                        result=0;
                        stat=NOP;
                    }
                    result=result*radix+nn;
                    if (result>MAXV) overflow=true;
                    else if (result<MINV) overflow=true;
                    display();
                }
            }
        }
    }
    class setRadix implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (hx.getState()) {
                radix=16;
            }else{
                radix=10;
            }
            hkeytop();
            display();
        }

    }
}