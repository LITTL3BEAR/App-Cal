package com.littl3bear.mycal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextView screen;
    private String show = "";
    private String operand = "";
    private String result = "";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen = findViewById(R.id.textScreen);
        screen.setText(show);
    }

    // ส่วนการคำนวณ
    private double calculate(String numA, String numB, String opr) {
        switch (opr) {
            case "+":
                return Double.valueOf(numA) + Double.valueOf(numB);
            case "-":
                return Double.valueOf(numA) - Double.valueOf(numB);
            case "*":
                return Double.valueOf(numA) * Double.valueOf(numB);
            case "/":
                try {
                    return Double.valueOf(numA) / Double.valueOf(numB);
                } catch (Exception e) {
                    screen.setText("ERROR");
                }
            default:
                return -1;
        }
    }

    // ส่งค่าคำนวณ แบ่งครึ่งด้วยเครื่องหมาย เช่น 123-321 -> [123],[-],[321]
    private boolean getResult() {
        // ถ้ายังไม่มีเครื่องหมาย
        if (operand == "") return false;
        String[] text = show.split(Pattern.quote(operand));
        // ถ้าตัวเลขไม่ครบ 2 ค่า
        if (text.length < 2) return false;
        result = String.valueOf(calculate(text[0], text[1], operand));
        // ตัด .0 ออก
        char c = result.charAt(result.length() - 1);
        if (c == '0') result = result.substring(0, result.length() - 2);

        return true;
    }

    // ส่วนตัวเลข
    public void onClickNumber(View v) {
        if (result != "") {
            show = "";
            operand = "";
            result = "";
        }
        Button b = (Button) v;
        show += b.getText();
        screen.setText(show);
    }

    // ตรวจสอบเครื่องหมาย
    private boolean isOperand(char opr) {
        switch (opr) {
            case '+':
                return true;
            case '-':
                return true;
            case '*':
                return true;
            case '/':
                return true;
            default:
                return false;
        }
    }

    // ส่วนเครื่องหมาย
    public void onClickOperator(View v) {
        // ถ้ามีคำตอบ เอาคำตอบมาคำนวณด้วย
        if (result != "") {
            String temp = result;
            show = "";
            operand = "";
            result = "";
            show = temp;
        }
        // ถ้าไม่มีตัวเลขอยู่ ห้ามใส่เครื่องหมาย
        if (show == "") return;
        Button b = (Button) v;
        // ถ้ามีเครื่องหมายแล้ว if:ใส่แทนที่ | else:คำนวณต่อจากคำตอบเดิม
        if (operand != "") {
            if (isOperand(show.charAt(show.length() - 1))) {
                show = show.replace(show.charAt(show.length() - 1), b.getText().charAt(0));
                screen.setText(show);
                return;
            } else {
                getResult();
                show = result;
                result = "";
            }
        }
        operand = b.getText().toString();
        show += b.getText();
        screen.setText(show);
    }

    // ปุ่มจุด
    public void onClickDot(View v) {
        // ถ้าไม่มีตัวเลขอยู่ข้างหน้า .234
        if (show == "") return;
        // ถ้าข้างหน้าเป้นเครื่องหมาย 234-.
        if (isOperand(show.charAt(show.length() - 1))) return;
        Button b = (Button) v;
        show += b.getText();
        screen.setText(show);
    }

    // ปุ่มเท่ากับ
    public void onClickEqual(View v) {
        if (show == "") return;
        if (!getResult()) return;
        screen.setText(result);
    }

    // ปุ่มล้าง
    public void onClickClear(View v) {
        show = "";
        operand = "";
        result = "";
        screen.setText(show);
    }

    // ปุ่มลบ
    public void onClickDel(View v) {
        // กันการลบเกิน
        if (show.length() < 2) return;
        // กันการลบเกินเครื่องหมาย
        if (operand != "") {
            char c = operand.charAt(0);
            char temp = show.charAt(show.length() - 1);
            if (c == temp) return;
        }
        show = show.substring(0, show.length() - 1);
        screen.setText(show);
    }
}
