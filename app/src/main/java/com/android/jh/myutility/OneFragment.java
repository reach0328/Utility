package com.android.jh.myutility;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class OneFragment extends Fragment implements View.OnClickListener{
    Button btn0, btn1,btn2,btn3,btn4,btn5,btn6, btn7,btn8,btn9,btnplus,btnsub,btnmul,btndiv,btnrun,btncancle;
    TextView tv,pv;
    String result= "",pre ="";
    ArrayList<String> cacu = new ArrayList<>();

    public OneFragment() {
        // Required empty public constructor
    }
    // Fragment가 호출 될때 재사용을 위해 전역으로 선언
    // ex)
    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { //전의 뷰에 대한 정보를 가지고 있는다
        //만약 view가 사용중이 던 것이 있다면 전역으로 선언 되어있던 view를 호출
        // 뷰를 재사용하기위해  Holder 형태로 만들어준다.
        if(view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_one, container, false);
        btn0 = (Button)view.findViewById(R.id.button0);
        btn1 = (Button)view.findViewById(R.id.button1);
        btn2 = (Button)view.findViewById(R.id.button2);
        btn3 = (Button)view.findViewById(R.id.button3);
        btn4 = (Button)view.findViewById(R.id.button4);
        btn5 = (Button)view.findViewById(R.id.button5);
        btn6 = (Button)view.findViewById(R.id.button6);
        btn7 = (Button)view.findViewById(R.id.button7);
        btn8 = (Button)view.findViewById(R.id.button8);
        btn9 = (Button)view.findViewById(R.id.button9);
        btnrun = (Button)view.findViewById(R.id.btnrun);
        btncancle = (Button)view.findViewById(R.id.btncancel);
        btnplus = (Button)view.findViewById(R.id.btnplus);
        btndiv = (Button)view.findViewById(R.id.btndiv);
        btnsub = (Button)view.findViewById(R.id.btnsub);
        btnmul = (Button)view.findViewById(R.id.btnmul);
        tv = (TextView) view.findViewById(R.id.textView2);
        pv = (TextView)view.findViewById(R.id.textView4);
        tv.setText(result);
        pv.setText(result);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnrun.setOnClickListener(this);
        btncancle.setOnClickListener(this);
        btnplus.setOnClickListener(this);
        btndiv.setOnClickListener(this);
        btnsub.setOnClickListener(this);
        btnmul.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button0 :
                result =result +"0";
                pre = pre+"0";
                tv.setText(result);
                pv.setText(pre);
                break;
            case R.id.button1:
                result =result +"1";
                pre = pre+"1";
                tv.setText(result);
                pv.setText(pre);
                break;
            case R.id.button2:
                result =result +"2";
                pre = pre+"2";
                tv.setText(result);
                pv.setText(pre);
                break;
            case R.id.button3:
                result =result +"3";
                pre = pre+"3";
                tv.setText(result);
                pv.setText(pre);
                break;
            case R.id.button4:
                result =result +"4";
                pre = pre+"4";
                tv.setText(result);
                pv.setText(pre);
                break;
            case R.id.button5:
                result =result +"5";
                pre = pre+"5";
                tv.setText(result);
                pv.setText(pre);
                break;
            case R.id.button6:
                result =result +"6";
                pre = pre+"6";
                tv.setText(result);
                pv.setText(pre);
                break;
            case R.id.button7:
                result =result +"7";
                pre = pre+"7";
                tv.setText(result);
                pv.setText(pre);
                break;
            case R.id.button8:
                result =result +"8";
                pre = pre+"8";
                tv.setText(result);
                pv.setText(pre);
                break;
            case R.id.button9:
                result =result +"9";
                pre = pre+"9";
                tv.setText(result);
                pv.setText(pre);
                break;
            case R.id.btncancel:
                result = "";
                pre ="";
                cacu.clear();
                tv.setText(result);
                pv.setText(pre);
                break;
            case R.id.btnrun:
                cacu.add(result);
                double re =0;
                for(int i=1;i<cacu.size();i=i+2){
                    if(cacu.get(i).equals("*")){
                        double temp1 = Double.parseDouble(cacu.get(i-1));
                        double temp2 = Double.parseDouble(cacu.get(i+1));
                        temp1 = temp1*temp2;
                        cacu.set(i-1,temp1+"");
                        cacu.remove(i);
                        cacu.remove(i);
                        i= i-2;
                    } else if(cacu.get(i).equals("/")){
                        double temp1 = Double.parseDouble(cacu.get(i-1));
                        double temp2 = Double.parseDouble(cacu.get(i+1));
                        temp1 = temp1/temp2;
                        cacu.set(i-1,temp1+"");
                        cacu.remove(i);
                        cacu.remove(i);
                        i=i-2;
                    }
                }
                re = Double.parseDouble(cacu.get(0));
                for (int i =1;i<cacu.size();i+=2) {

                    if (cacu.get(i).equals("+")) {
                        double temp2 = Double.parseDouble(cacu.get(i + 1));
                        re = re + temp2;
                    } else if (cacu.get(i).equals("+")) {
                        double temp2 = Double.parseDouble(cacu.get(i + 1));
                        re = re  - temp2;
                    }
                }
                result = re +"";
                tv.setText(result);
                break;
            case R.id.btnmul:
                cacu.add(result);
                cacu.add("*");
                pre = pre +"*";
                pv.setText(pre);
                result ="";
                break;
            case R.id.btndiv:
                cacu.add(result);
                cacu.add("/");
                pre = pre +"/";
                pv.setText(pre);
                result = "";
                break;
            case R.id.btnplus:
                cacu.add(result);
                cacu.add("+");
                pre = pre +"+";
                pv.setText(pre);
                result = "";
                break;
            case R.id.btnsub:
                cacu.add(result);
                cacu.add("-");
                pre = pre +"-";
                pv.setText(pre);

                result = "";
                break;
        }
    }
}
