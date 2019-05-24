package com.example.android_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Add_Person extends AppCompatActivity {

    ImageButton back;
    EditText editName;
    Button buttonContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        editName =(EditText)findViewById(R.id.editName);
        back =(ImageButton) findViewById(R.id.back);
        buttonContinue=(Button)findViewById(R.id.continueAdd);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back =new Intent(Add_Person.this, Persons_List.class);
                startActivity(back);
            }
        });

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    buttonContinue.setEnabled(false);
                    buttonContinue.setTextColor(Color.parseColor("#71a0b3"));
                    buttonContinue.setBackgroundResource(R.drawable.background_button_continue_disable);
                } else {
                    buttonContinue.setEnabled(true);
                    buttonContinue.setTextColor(Color.parseColor("#FFFFFF"));
                    buttonContinue.setBackgroundResource(R.drawable.background_button_connect);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                editName.setText("");
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                Intent intent =new Intent(getBaseContext(),Persons_List.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

    }
}
