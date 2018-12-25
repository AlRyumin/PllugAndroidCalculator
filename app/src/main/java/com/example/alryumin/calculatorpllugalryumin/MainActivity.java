package com.example.alryumin.calculatorpllugalryumin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9,
            buttonClear, buttonMultiple, buttonDevide, buttonAdd, buttonSubtract;
    private TextView inputField, resultField;
    private boolean isFirst = true;
    private String firstNumber = "", secondNumber = "", inputText = "", resultText = "";

    private char operation = '\0';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        if (savedInstanceState != null){
                setBundledFields(savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setEventListener();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_clear){
            clearAll();
            return;
        }

        if(view.getId() == R.id.button_result){
            countResult();
            return;
        }

        setValue(view);
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable("isFirst", isFirst);
        state.putSerializable("firstNumber", firstNumber);
        state.putSerializable("secondNumber", secondNumber);
        state.putSerializable("inputText", inputText);
        state.putSerializable("resultText", resultText);
        state.putSerializable("operation", operation);
    }

    protected void setBundledFields(Bundle savedInstanceState){
        if (savedInstanceState.getSerializable("isFirst") != null) {
            isFirst = (boolean) savedInstanceState.getSerializable("isFirst");
        }

        if (savedInstanceState.getSerializable("firstNumber") != null) {
            firstNumber = (String) savedInstanceState.getSerializable("firstNumber");
        }

        if (savedInstanceState.getSerializable("secondNumber") != null) {
            secondNumber = (String) savedInstanceState.getSerializable("secondNumber");
        }

        if (savedInstanceState.getSerializable("operation") != null) {
            operation = (char) savedInstanceState.getSerializable("operation");
        }

        if (savedInstanceState.getSerializable("inputText") != null) {
            inputText = (String) savedInstanceState.getSerializable("inputText");
            inputField.setText(inputText);
        }

        if (savedInstanceState.getSerializable("resultText") != null) {
            resultText = (String) savedInstanceState.getSerializable("resultText");
            resultField.setText(resultText);
        }
    }

    protected boolean isNumericId(View view) {
        Button button = (Button) view;

        try{
            int i = Integer.parseInt(button.getText().toString());
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    
    protected void setValue(View view){
        boolean isNumeric = isNumericId(view);

        if(isNumeric) {
            setNumber(view);
            return;
        } else {
            setOperation(view);
            return;
        }
    }
    
    protected void setOperation(View view){

        if(firstNumber.isEmpty() || !secondNumber.isEmpty()){
            return;
        } else if(!isFirst) {
            inputText = removeLastChar(inputText);
        } else {
            isFirst = false;
        }

        if(view.getId() == R.id.button_multiple){
            operation = '*';
        } else if(view.getId() == R.id.button_devide){
            operation = '/';
        } else if(view.getId() == R.id.button_add){
            operation = '+';
        } else if (view.getId() == R.id.button_subtract){
            operation = '-';
        }

        Button button = (Button) view;
        inputText += button.getText().toString();
        inputField.setText(inputText);

        return;
    }

    protected void clearAll(){
        inputField.setText("");
        resultField.setText("");
        isFirst = true;

        firstNumber = secondNumber = inputText = resultText = "";
    }

    protected void countResult(){
        try {
            double a = Double.parseDouble(firstNumber);
            double b = Double.parseDouble(secondNumber);

            double result = 0;

            switch (operation) {
                case '*':
                    result = a * b;
                    break;
                case '/':
                    result = a / b;
                    break;
                case '+':
                    result = a + b;
                    break;
                case '-':
                    result = a - b;
                    break;
                default:
                    break;
            }

            resultText = Double.toString(result);

            firstNumber = resultText;
            secondNumber = "";
            isFirst = true;
            operation = '\0';

            resultField.setText(resultText);

        } catch (Exception e){
            Log.d("countResult Exception", e.getLocalizedMessage());

            return;
        }
    }

    protected void setNumber(View view){
        Button button = (Button) view;
        if(!isFirst && operation == '\0'){
            return;
        }

        if(isFirst){
            firstNumber += button.getText().toString();
        } else {
            secondNumber += button.getText().toString();
        }

        inputText += button.getText().toString();
        inputField.setText(inputText);
    }

    protected String removeLastChar(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private void initView() {
        button0 = (Button) findViewById(R.id.button_0);
        button1 = (Button) findViewById(R.id.button_1);
        button2 = (Button) findViewById(R.id.button_2);
        button3 = (Button) findViewById(R.id.button_3);
        button4 = (Button) findViewById(R.id.button_4);
        button5 = (Button) findViewById(R.id.button_5);
        button6 = (Button) findViewById(R.id.button_6);
        button7 = (Button) findViewById(R.id.button_7);
        button8 = (Button) findViewById(R.id.button_8);
        button9 = (Button) findViewById(R.id.button_9);
        buttonClear = (Button) findViewById(R.id.button_clear);
        buttonMultiple = (Button) findViewById(R.id.button_multiple);
        buttonDevide = (Button) findViewById(R.id.button_devide);
        buttonAdd = (Button) findViewById(R.id.button_add);
        buttonSubtract = (Button) findViewById(R.id.button_subtract);

        inputField = (TextView) findViewById(R.id.input_field);
        resultField = (TextView) findViewById(R.id.result_field);
    }

    private void setEventListener() {
        ViewGroup group = (ViewGroup) findViewById(R.id.main_layout);
        View v;

        for (int i = 0; i < group.getChildCount(); i++) {
            v = group.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(this);
            } else if (hasChildren((View) v)) {
                setEventListener((View) v);
            }
        }
    }

    private void setEventListener(View view) {
        try {
            ViewGroup group = (ViewGroup) view;
            View v;

            for (int i = 0; i < group.getChildCount(); i++) {
                v = group.getChildAt(i);
                if (v instanceof Button) {
                    v.setOnClickListener(this);
                } else if (hasChildren((View) v)) {
                    setEventListener((View) v);
                }
            }
        } catch (Exception e) {
            Log.d("setEventListener Exc: ", e.getMessage());
            return;
        }
    }

    private boolean hasChildren(View view) {
        ViewGroup group = (ViewGroup) view;
        return group.getChildCount() > 0;
    }
}
