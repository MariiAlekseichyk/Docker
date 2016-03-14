package com.example.android.justjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public void submitOrder (View view) {
        int quantity = 4 ;
        display(quantity);
        displayPrice (quantity * 5);
        }


public class MainActivity extends AppCompatActivity {

    @Overrideverrid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
