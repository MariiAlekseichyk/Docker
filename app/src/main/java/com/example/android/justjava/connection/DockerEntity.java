package com.example.android.justjava.connection;


import android.app.ProgressDialog;

import com.example.android.justjava.R;
import com.example.android.justjava.main.SessionActivity;
import com.example.android.justjava.main.activity_images;
import com.example.android.justjava.main.card_item_info;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;


public class DockerEntity extends AppCompatActivity {

     /*********************************************
     *
     * DEFINING CONSTANTS AND PARAMS
     *
     * Docker MM. Proof of Concept. Pray for work.
     * Author: Mariia Alekseychik
     *********************************************/

    //Need to move constants in a separate file. (@_@)
    public final static int DOCKER_TEST_SILENT = -2;
    public final static int DOCKER_TEST = -1;
    public final static int DOCKER_INFO = 0;
    public final static int DOCKER_IMAGES = 1;
    // public final static int DOCKER_CONTAINERS = 2; // RESERVED >_>


    //DockerURL
    protected String connectionHost ="http://";
    protected Context forManipulations;
    protected View someView;
    //Parsed value arrays
    public ArrayList<HashMap<String, String>> parsedList;
    public HashMap<String, String> dockerNode;
    //protected ArrayList<HashMap<String, String>> containersList; // RESERVED <_<

    protected ProgressDialog pDialog;

    //Generic API tags
    protected static final String TAG_ID = "Id";
    protected static final String TAG_NAME = "Name";

    //Docker API
    protected static final String TAG_CONTAINERS_COUNT = "Containers";
    protected static final String TAG_IMAGES_COUNT = "Images";
    protected static final String TAG_KERNEL_VERSION = "KernelVersion";
    protected static final String TAG_OS = "OperatingSystem";


    //Docker image API tags
    protected static final String TAG_REPOTAGS = "RepoTags";
    protected static final String TAG_CREATED = "Created";
    protected static final String TAG_VIRTUALSIZE ="VirtualSize";
    SharedPreferences.Editor editor;



    //API ENDPOINTS
    private static final String API_LIST_IMAGES = "/images/json";
    //Private static final String API_LIST_CONTRAINERS = "/containers/json";
    private static final String API_LIST_INFO = "/info";

    public static ProgressDialog initProgDialogs (Context context){
        ProgressDialog pDial = new ProgressDialog(context, R.style.AlertDialogStyle);
        pDial.setMessage("Please wait...");
        pDial.setCancelable(false);

        return pDial;
    }


    /**
     * Method which navigates to Home Activity
     */

    public static void navigatetoHomeActivity(Context context){
        Intent homeIntent = new Intent(context,SessionActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(homeIntent);

    }


    //Constructor
    public DockerEntity(String host, String portNum, ProgressDialog pDialog) {
        try {
            forManipulations = pDialog.getContext();
            this.pDialog = pDialog;
            dockerNode = new HashMap<String, String>();

            if (portNum!=null) {portNum=":" + portNum;};
            connectionHost += host + portNum;
            editor = forManipulations.getSharedPreferences("docker_host", 0).edit();
            editor.putString("ConnectionInfo",connectionHost);
            editor.apply();

                if (connectionHost.equals("http://")) {
                throw new CustomException("Empty connection info ....");
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    //Constructor #2
    public DockerEntity(String fullHost, ProgressDialog pDialog) {
        try {
            forManipulations = pDialog.getContext();
            this.pDialog = pDialog;
            dockerNode = new HashMap<String, String>();
            parsedList = new ArrayList<HashMap<String, String>>();
            if (fullHost!=null) connectionHost= fullHost;
            else {
                throw new CustomException("Empty connection info ....");
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }


    //API call to docker
    public ArrayList<HashMap<String, String>> returnParsed () {
        return parsedList;
    }

    protected ResponseHandlerInterface initInterface (final int reqID){

        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject c = response.getJSONObject(i);

                        // tmp hashmap for single node
                        HashMap<String, String> node = new HashMap<String, String>();

                        //select what we are loking for
                        switch (reqID) {
                            case DOCKER_IMAGES:
                                //Parsing Images
                                String id = c.getString(TAG_ID);
                                String name = c.getString(TAG_REPOTAGS);
                                String imageDate = c.getString(TAG_CREATED);
                                imageDate = convertUnixDate(Long.parseLong(imageDate, 10));
                                String size = c.getString(TAG_VIRTUALSIZE);

                                // adding each child node to HashMap key => value
                                node.put(TAG_ID, id);
                                node.put(TAG_REPOTAGS, name);
                                node.put(TAG_CREATED, imageDate);
                                node.put(TAG_VIRTUALSIZE, size);
                                break;

                            case 2:
                                // Space for containers and future parsing
                                break;

                            default:
                                pDialog.hide();
                                throw new JSONException("Incorrect option selection......");
                        }
                        // adding parsed object to the list
                        parsedList.add(node);
                    }
                    pDialog.hide();
                    SessionActivity.jumpToImg((ArrayList<HashMap<String, String>>) parsedList,someView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                try {
                    //select what we are loking for
                    switch (reqID) {
                        case DOCKER_INFO:
                            //Parsing Images
                            String name = response.getString(TAG_NAME);
                            String os = response.getString(TAG_OS);
                            String kernelVer = response.getString(TAG_KERNEL_VERSION);
                            String contCount = response.getString(TAG_CONTAINERS_COUNT);
                            String imgCount = response.getString(TAG_IMAGES_COUNT);


                            //Add values to node.
                            dockerNode.put(TAG_NAME, name);
                            dockerNode.put(TAG_OS, os);
                            dockerNode.put(TAG_KERNEL_VERSION, kernelVer);
                            dockerNode.put(TAG_CONTAINERS_COUNT, contCount);
                            dockerNode.put(TAG_IMAGES_COUNT, imgCount);

                            pDialog.hide();
                            break;

                        case DOCKER_TEST:
                            pDialog.hide();
                            Toast.makeText(forManipulations, "Connection succeeded", Toast.LENGTH_LONG).show();
                            break;
                        case DOCKER_TEST_SILENT:
                            //Travel to home activity
                            pDialog.hide();
                            navigatetoHomeActivity(forManipulations);
                        default:
                            pDialog.hide();
                            throw new JSONException("Incorrect option selection......");

                    }
                    // adding parsed object to the list
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse){
                pDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(forManipulations, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(forManipulations, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(forManipulations, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }

        };

    }

    public void getApiRequest(final int reqID, View v){
        someView = v;
        getApiRequest(reqID);

    }



    public void getApiRequest(final int reqID) {
        try {
            String url;
            pDialog.show();
            ResponseHandlerInterface intrf =initInterface(reqID);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setMaxRetriesAndTimeout(1,15000);

            switch (reqID) {
                case DOCKER_INFO:case DOCKER_TEST:case DOCKER_TEST_SILENT:
                    url = connectionHost + API_LIST_INFO;
                    break;
                case DOCKER_IMAGES:
                    url = connectionHost + API_LIST_IMAGES;
                    break;
                default:
                    throw new CustomException("Incorrect get request....");
            }


            //Do the GET request ###############
            client.get(url, intrf);

        }catch (CustomException e) {
            e.printStackTrace();
        }
    }


    //Convert to understandable date
    protected String convertUnixDate (long unixSeconds){
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        return  sdf.format(date);
    }

    //Docked init exception
    class CustomException extends Exception
    {
        public CustomException (String message)
        {
            super(message);
        }
    }






}

