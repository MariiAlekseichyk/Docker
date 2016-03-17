package com.example.android.justjava.main;

import android.content.Intent;
import com.example.android.justjava.R;
import com.example.android.justjava.connection.DockerEntity;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.HashMap;

public class card_item_info extends AppCompatActivity {

    HashMap <String, String> info;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_item_info);
        Intent intent = getIntent();
        info = (HashMap<String, String>)intent.getSerializableExtra("hash");
        setInfo(info);
    }


    public void setInfo (HashMap<String, String> inpt) {
        TextView txt = (TextView) findViewById(R.id.os_holder_val);
        txt.setText(inpt.get("OperatingSystem"));
        txt = (TextView) findViewById(R.id.kernel_holder_val);
        txt.setText(inpt.get("KernelVersion"));
        txt = (TextView) findViewById(R.id.docker_hostname);
        txt.setText(inpt.get("Name"));
        txt = (TextView) findViewById(R.id.con_cun_val);
        txt.setText(inpt.get("Containers"));
        txt = (TextView) findViewById(R.id.img_count_val);
        txt.setText(inpt.get("Images"));
    }


}
