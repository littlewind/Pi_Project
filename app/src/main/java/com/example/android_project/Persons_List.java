package com.example.android_project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class Persons_List extends AppCompatActivity {

    Button done;
    Button addNewPerson;
    ArrayList<Person> arrayPerson = new ArrayList<>();
    Persons_Adapter adapter;
    DatabasePerson databasePerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons_list);

        addNewPerson =(Button)findViewById(R.id.addNewPerson);
        done =(Button)findViewById(R.id.done);
        databasePerson= new DatabasePerson(getBaseContext());

        databasePerson.QueryData("create table if not exists Person(id integer primary key autoincrement, name text, image int);");


        //check database. Nếu database chưa có dữ liệu. Thêm dữ liệu vào
        if(databasePerson.isEmpty()==1)
        {
            databasePerson.insertPerson("",0);
            databasePerson.insertPerson("",0);
            databasePerson.insertPerson("",0);
            databasePerson.insertPerson("",0);
            databasePerson.insertPerson("",0);
            databasePerson.insertPerson("",0);
            databasePerson.insertPerson("",0);
            databasePerson.insertPerson("",0);
            databasePerson.insertPerson("",0);
            databasePerson.insertPerson("",0);
        }

        //Lấy dữ liệu từ database vào mảng arrayPerson
        databasePerson.getDataBase(arrayPerson);

        //Kiểm tra có dữ liệu nhận vào
        checkReceivedData();

        //đổ dữ liệu lên gridview
        GridView gridView = (GridView) findViewById(R.id.persons);
        adapter= new Persons_Adapter(arrayPerson);
        gridView.setAdapter(adapter);



        addNewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(getBaseContext(), Add_Person.class);
                startActivity(add);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Persons_List.this,Menu.class);
                startActivity(back);
            }
        });

        //Khi người dùng nhần giữ gridview để xóa person
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                if(arrayPerson.get(position).getImage()==1) {
                    final Dialog dialog = new Dialog(Persons_List.this);
                    dialog.setContentView(R.layout.dialog_delete);
                    //                View v= getWindow().getDecorView();
                    //                v.setBackgroundResource(android.R.color.transparent);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    Button cancel = (Button) dialog.findViewById(R.id.cancel);
                    Button delete = (Button) dialog.findViewById(R.id.delete);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //close dialog
                            dialog.dismiss();
                        }
                    });

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            arrayPerson.remove(position);
                            arrayPerson.add(new Person("",0));
                            checkButtonAddPerson();
                            databasePerson.setDataBase(arrayPerson);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                }
                return false;
            }
        });

    }
    public void checkButtonAddPerson()
    {
        int sum=arrayPerson.size();
        int index=0;
        for(int i=0;i<sum;i++)
        {
            if(arrayPerson.get(i).getImage()==1)
            {
                index++;
            }
        }
        if(index<sum)
        {
            addNewPerson.setEnabled(true);
            addNewPerson.setTextColor(Color.parseColor("#FFFFFF"));
            addNewPerson.setBackgroundResource(R.drawable.background_button_connect);
        }
        else if(index >=sum)
        {
            addNewPerson.setEnabled(false);
            addNewPerson.setTextColor(Color.parseColor("#71a0b3"));
            addNewPerson.setBackgroundResource(R.drawable.background_button_add_disable);
        }
    }
    public void checkReceivedData()
    {
        //Nhan du lieu tu context: add_person
        Intent receivedIntent =getIntent();
        String name = receivedIntent.getStringExtra("name");
        //kiểm tra dữ liệu nhận về. Nếu !null: thêm vào mảng person và cập nhật database
        if(name!=null)
        {
            int number= arrayPerson.size();
            for(int i=0;i<number;i++)
            {
                //Thêm vào arrayPerson
                if(arrayPerson.get(i).getImage()==0)
                {
                    arrayPerson.get(i).setName(name);
                    arrayPerson.get(i).setImage(1);
                    break;
                }
            }
            databasePerson.setDataBase(arrayPerson);
//            adapter.notifyDataSetChanged();
            checkButtonAddPerson();
        }
        else //ktra mang co bao nhieu phan tu: neu >=6: setButton=false
            checkButtonAddPerson();
    }
}
