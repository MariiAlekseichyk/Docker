package com.example.android.justjava.main;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.example.android.justjava.R;
import com.example.android.justjava.connection.DockerEntity;

import java.util.ArrayList;
import java.util.HashMap;


public class SessionActivity extends AppCompatActivity {

    protected ProgressDialog pDialog;
    protected DockerEntity connTestEnt;
    protected SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        pDialog = DockerEntity.initProgDialogs(SessionActivity.this);
        prefs = getSharedPreferences("docker_host", MODE_PRIVATE);
        String restoredHost = prefs.getString("ConnectionInfo", null);
        connTestEnt = new DockerEntity(restoredHost, pDialog);
        connTestEnt.getApiRequest(DockerEntity.DOCKER_INFO);
    }


    public void getDockerInfo(View v) {

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SessionActivity.this, v, "cardImage");
        Intent intent = new Intent(SessionActivity.this, card_item_info.class);
        intent.putExtra("hash", connTestEnt.dockerNode);
        startActivity(intent, options.toBundle());
    }

    public void gcForImages(View v) {

        connTestEnt.getApiRequest(DockerEntity.DOCKER_IMAGES);

    }

    public static void jumpToImg (ArrayList<HashMap<String,String>> arr, View v){
        Activity context = (Activity) v.getContext();
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(context, v, "cardImage");
        Intent intent = new Intent(context, activity_images.class);
        intent.putExtra("hashArr", arr);
        context.startActivity(intent, options.toBundle());
    }

}


