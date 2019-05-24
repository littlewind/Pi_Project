package com.example.android_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class Menu extends AppCompatActivity {

    LinearLayout face_Recognition;
    LinearLayout add_Person;
    LinearLayout remoteControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
      //  getSupportActionBar().hide();
        add_Person=(LinearLayout) findViewById(R.id.add_person);
        remoteControl= (LinearLayout) findViewById(R.id.control);

        add_Person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Persons_List.class);
                startActivity(intent);
            }
        });

        remoteControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ControlPi.class);
                startActivity(intent);
            }
        });

    }
}
