package com.example.todolist7;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> ToDoList = null;
    ArrayAdapter<String> adapter = null;
    ListView lv = null;
    ImageButton FAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToDoList = getArrayVal(getApplicationContext());
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ToDoList);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItem.trim().equals(ToDoList.get(position).trim())) {
                    removeTask(selectedItem, position);
                } else {
                    Toast.makeText(getApplicationContext(),"Error Removing Element", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, final int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItem.trim().equals(ToDoList.get(position).trim())) {
                    editTask(selectedItem, position);
                } else {
                    Toast.makeText(getApplicationContext(),"Error Removing Element", Toast.LENGTH_LONG).show();
                }
            }
        });

        FAB = (ImageButton) findViewById(R.id.imageButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    public void addTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Task");
        final EditText inputField = new EditText(this);
        builder.setView(inputField);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String task = inputField.getText().toString();
                ToDoList.add(preferredCase(task));
                storeArrayVal(ToDoList, getApplicationContext());
                lv.setAdapter(adapter);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    public void editTask(String selectedItem, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit " + selectedItem);
        final EditText inputField = new EditText(this);
        builder.setView(inputField);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToDoList.set(position,preferredCase(inputField.getText().toString()));
                storeArrayVal(ToDoList, getApplicationContext());
                lv.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void removeTask(String selectedItem, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove " + selectedItem + "?");
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToDoList.remove(position);
                Collections.sort(ToDoList);
                storeArrayVal(ToDoList, getApplicationContext());
                lv.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public static String preferredCase(String original)
    {
        if (original.isEmpty())
            return original;

        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static ArrayList getArrayVal( Context dan)
    {
        SharedPreferences WordSearchGetPrefs = dan.getSharedPreferences("dbArrayValues",Activity.MODE_PRIVATE);
        Set<String> tempSet = new HashSet<>();
        tempSet = WordSearchGetPrefs.getStringSet("myArray", tempSet);
        return new ArrayList<>(tempSet);
    }

    public static void storeArrayVal( ArrayList<String> inArrayList, Context context)
    {
        Set<String> WhatToWrite = new HashSet<>(inArrayList);
        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchPutPrefs.edit();
        prefEditor.putStringSet("myArray", WhatToWrite);
        prefEditor.commit();
    }
}