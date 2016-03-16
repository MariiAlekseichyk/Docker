package com.example.android.justjava.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.example.android.justjava.R;
import com.example.android.justjava.connection.DockerEntity;


public class MainActivity extends AppCompatActivity {

    protected EditText tvHostname;
    protected EditText tvPort;
    protected EditText tvConnectionName;
    protected CheckBox saveConn;
    protected String hostnameAddr, portNum, conName;
    protected ProgressDialog pDialog;
    protected DockerEntity connTestEnt;
    protected  boolean  saveConnData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //input
        tvHostname = (EditText) findViewById(R.id.hostname);
        tvPort = (EditText) findViewById(R.id.port);
        tvConnectionName = (EditText) findViewById(R.id.connection_name);

        //checkbox
        saveConn = (CheckBox) findViewById(R.id.checkBox);

        //dialog
        pDialog = DockerEntity.initProgDialogs(MainActivity.this);

    }

    /**
     * Method which navigates from Login Activity to Home Activity
     */

    public static void navigatetoHomeActivity(Context context){
        Intent homeIntent = new Intent(context,SessionActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(homeIntent);
    }

    protected void getInputData (){
        //get values
        hostnameAddr = tvHostname.getText().toString();
        Log.d("hostname add ", "> "+ hostnameAddr );
        if (hostnameAddr.isEmpty()| hostnameAddr.contains(" ")){
            Toast.makeText(MainActivity.this, "Hostname can not be empty or have whitespace char", Toast.LENGTH_LONG).show();
            tvHostname.setText(null);
        }else {
            portNum = tvPort.getText().toString();
            Log.d("port ", "> "+portNum );
            conName = tvConnectionName.getText().toString();
            Log.d("conn name", "> "+conName);
        }
    }

    //button handlers
    public void testConnectionHandler (View v){
        Context current = v.getContext();
        getInputData();
        connTestEnt = new DockerEntity(hostnameAddr, portNum, current, pDialog);
        connTestEnt.getApiRequest(DockerEntity.DOCKER_TEST);
    }

    public void makeConnection (View v){
        Context current = v.getContext();
        getInputData();
        connTestEnt = new DockerEntity(hostnameAddr, portNum, current, pDialog);
        connTestEnt.getApiRequest(DockerEntity.DOCKER_TEST_SILENT);
    }

}

