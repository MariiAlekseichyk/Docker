package com.example.android.justjava.main;

import android.content.Intent;
import com.example.android.justjava.R;
import com.example.android.justjava.connection.imageAdapter;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class activity_images extends AppCompatActivity {

    ArrayList<HashMap<String, String>> images;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        Intent test = getIntent();
        images =(ArrayList<HashMap<String, String>>) test.getParcelableExtra("hashArr");
        RecyclerView rvImages = (RecyclerView) findViewById(R.id.rvImages);
        imageAdapter adapter = new imageAdapter(images);

        rvImages.setAdapter(adapter);
        rvImages.setLayoutManager(new LinearLayoutManager(this));


    }

}