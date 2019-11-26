package com.example.bandile.groupus;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bandile.groupus.utils.GroupCustomAdapter;
import com.example.bandile.groupus.utils.NameCustomAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button group;
    Button clear;
    ImageButton add;
    ListView listview;
    TextView name;
    ArrayList<String> names;
    ArrayList<String> tempNames;
    ArrayList<String> groups;
    ArrayAdapter<String> adapterList;
    ArrayAdapter<String> adapterSpinner;
    Random r;
    int random;
    Spinner spinner;
    ArrayList<String> spinnerArrayEven;
    ArrayList<String> spinnerArrayOdd;
    boolean grouped = false;
    private static NameCustomAdapter nameAdapter;
    private static GroupCustomAdapter groupAdapter;
    MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        group = (Button) findViewById(R.id.group);
        clear = (Button) findViewById(R.id.clear);
        add = (ImageButton) findViewById(R.id.imageButtonAdd);
        listview = (ListView) findViewById(R.id.list);
        name = (TextView) findViewById(R.id.name);
        names = new ArrayList<>();
        tempNames = new ArrayList<>();
        groups = new ArrayList<>();
        r = new Random();
        spinner = (Spinner) findViewById(R.id.spinner);
        main = this;

        nameAdapter= new NameCustomAdapter(names,getApplicationContext(),this);
        listview.setAdapter(nameAdapter);

        //adapterList = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, names);
        //listview.setAdapter(adapterList);

        spinnerArrayEven =  new ArrayList<String>();
        spinnerArrayEven.add("2");
        spinnerArrayEven.add("4");
        spinnerArrayEven.add("6");
        spinnerArrayEven.add("8");
        spinnerArrayEven.add("10");

        spinnerArrayOdd =  new ArrayList<String>();
        spinnerArrayOdd.add("1");
        spinnerArrayOdd.add("3");
        spinnerArrayOdd.add("5");
        spinnerArrayOdd.add("7");
        spinnerArrayOdd.add("9");

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(grouped){
                    random = 0;
                    groups.clear();

                    nameAdapter= new NameCustomAdapter(names,getApplicationContext(),main);
                    listview.setAdapter(nameAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        name.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    addName();

                    return true;
                }
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {
                ((TextView) view).setTextColor(Color.BLACK);

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addName();
            }
        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupNames();
                grouped = true;

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                names.clear();
                random = 0;
                groups.clear();
                grouped = false;

                nameAdapter= new NameCustomAdapter(names,getApplicationContext(),main);
                listview.setAdapter(nameAdapter);

                //adapterList = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, names);
                //listview.setAdapter(adapterList);
            }
        });


    }

    private void groupNames() {
        int i = 0;
        String groupNames = "";
        int max = Integer.valueOf(spinner.getSelectedItem().toString());
        tempNames = names;
        while(!names.isEmpty()){

            for(int j = 0; j < max-1; j++){
                random = r.nextInt(names.size());
                while(groupNames.contains(names.get(random))){
                    random = r.nextInt(names.size());
                }
                groupNames = groupNames + names.get(random) + " - ";
                names.remove(random);
            }

            random = r.nextInt(names.size());
            while(groupNames.contains(names.get(random))){
                random = r.nextInt(names.size());
            }
            groupNames = groupNames + names.get(random);
            names.remove(random);

            groups.add(groupNames);

            groupNames = "";

            i = i + max;
        }

        names = tempNames;

        groupAdapter= new GroupCustomAdapter(groups,getApplicationContext());
        listview.setAdapter(groupAdapter);

        //adapterList = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, groups);
        //listview.setAdapter(adapterList);;
    }

    public void addName(){
        if(name.getText() != null && !name.getText().toString().trim().equals("")){
            if(!names.contains(name.getText().toString().trim())) {
                names.add(name.getText().toString().trim());
                nameAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(this, name.getText().toString().trim() + " is already in the list", Toast.LENGTH_LONG).show();
            }
            name.setText("");
        }
        if(names.size() % 2 == 0){
            adapterSpinner = new ArrayAdapter<String>(
                    MainActivity.this, android.R.layout.simple_spinner_item, spinnerArrayEven);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSpinner);
        }else{
            adapterSpinner = new ArrayAdapter<String>(
                    MainActivity.this, android.R.layout.simple_spinner_item, spinnerArrayOdd);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSpinner);
        }
    }

    public void deleteName(String pName){
        if(pName != null && !pName.equals("")){
            names.remove(pName.trim());
            nameAdapter.notifyDataSetChanged();
            name.setText("");
        }
        if(names.size() % 2 == 0){
            adapterSpinner = new ArrayAdapter<String>(
                    MainActivity.this, android.R.layout.simple_spinner_item, spinnerArrayEven);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSpinner);
        }else{
            adapterSpinner = new ArrayAdapter<String>(
                    MainActivity.this, android.R.layout.simple_spinner_item, spinnerArrayOdd);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSpinner);
        }
    }

}
