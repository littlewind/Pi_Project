package com.example.android_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Persons_Adapter extends BaseAdapter {

    ArrayList<Person> arrayList;

    public Persons_Adapter(ArrayList<Person> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class PersonHoleder
    {
        private TextView name;
        private ImageView image;

        PersonHoleder(View row)
        {
            name = (TextView)row.findViewById(R.id.name);;
            image=(ImageView)row.findViewById(R.id.image);
        }

        void populateFrom(Person person)
        {
            name.setText(person.getName());

            if(person.getImage()==1)
            {
                image.setImageResource(R.drawable.persons);
            }
            else
            {
                image.setBackgroundResource(R.drawable.background_member);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        row = layoutInflater.inflate(R.layout.person_background,null);

        TextView name = (TextView)row.findViewById(R.id.name);;
        ImageView image=(ImageView)row.findViewById(R.id.image);



        name.setText(arrayList.get(position).getName());

        if(arrayList.get(position).getImage()==1)
        {
            image.setImageResource(R.drawable.persons);
        }
        else
        {
            image.setBackgroundResource(R.drawable.background_member);
        }

        return row;
    }
}
