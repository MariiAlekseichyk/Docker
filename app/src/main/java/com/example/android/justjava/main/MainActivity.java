package com.example.android.justjava.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
    public static String MY_PREFS_NAME="DockerConnectionInfo";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        //input
        tvHostname = (EditText) findViewById(R.id.hostname);
        tvPort = (EditText) findViewById(R.id.port);
        tvConnectionName = (EditText) findViewById(R.id.connection_name);
        if(prefs.getBoolean("Save_Credentials_Option", false)){
            tvHostname.setText(prefs.getString("Hostname", null));
            tvPort.setText(prefs.getString("Port", null));
            tvConnectionName.setText(prefs.getString("ConnectionName", null));
        }


        //checkbox
        saveConn = (CheckBox) findViewById(R.id.checkBox);

        //dialog
        pDialog = DockerEntity.initProgDialogs(MainActivity.this);

    }

    protected boolean getInputData (){
        //get values
        hostnameAddr = tvHostname.getText().toString();
        Log.d("hostname add ", "> " + hostnameAddr);
        if (hostnameAddr.isEmpty()|| hostnameAddr.contains(" ")){
            Toast.makeText(MainActivity.this, "Hostname can not be empty or have whitespace char", Toast.LENGTH_LONG).show();
            tvHostname.setText("");
            return false;
        }else{
            portNum = tvPort.getText().toString();
            Log.d("port ", "> "+portNum );
            conName = tvConnectionName.getText().toString();
            Log.d("conn name", "> "+conName);
            connTestEnt = new DockerEntity(hostnameAddr, portNum, pDialog);
            if (saveConn.isChecked()){
                editor.putBoolean("Save_Credentials_Option",true);
                editor.putString("Hostname",hostnameAddr);
                editor.putString("Port",portNum);
                editor.putString("ConnectionName",conName);
                editor.commit();
            } else {
                editor.putBoolean("Save_Credentials_Option",false);
                editor.putString("Hostname",null);
                editor.putString("Port",null);
                editor.putString("ConnectionName",null);
                editor.commit();
            }

            return true;
        }
    }

    //button handlers
    public void testConnectionHandler (View v){
        Context current = v.getContext();
        if(getInputData()){
            connTestEnt.getApiRequest(DockerEntity.DOCKER_TEST);}
    }

    public void makeConnection (View v){
        Context current = v.getContext();
        if(getInputData()){
            connTestEnt.getApiRequest(DockerEntity.DOCKER_TEST_SILENT);
        }
    }

}

