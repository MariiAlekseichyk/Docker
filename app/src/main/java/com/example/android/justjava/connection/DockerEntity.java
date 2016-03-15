package com.example.android.justjava.connection;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


public class DockerEntity  {

    /*********************************************
     *
     * DEFINING CONSTANTS AND PARAMS
     *
     * Docker MM. Proof of Concept. Pray for work.
     * Author: Mariia Alekseychik
     *********************************************/

    //Need to move constants in a separate file. (@_@)

    public final static int DOCKER_INFO = 0;
    public final static int DOCKER_IMAGES = 1;
    // public final static int DOCKER_CONTAINERS = 2; // RESERVED >_>

    //DockerURL
    private String connectionHost ="http://";

    //Parsed value arrays
    public ArrayList<HashMap<String, String>> imagesList;
    public ArrayList<HashMap<String, String>> dockerInfo;
    //public ArrayList<HashMap<String, String>> containersList; // RESERVED <_<

    //Generic API tags
    private static final String TAG_ID = "Id";
    private static final String TAG_NAME = "Name";

    //Docker API
    private static final String TAG_CONTAINERS_COUNT = "Containers";
    private static final String TAG_IMAGES_COUNT = "Images";
    private static final String TAG_KERNEL_VERSION = "KernelVersion";
    private static final String TAG_OS = "OperatingSystem";


    //Docker image API tags
    private static final String TAG_REPOTAGS = "RepoTags";
    private static final String TAG_CREATED = "Created";
    private static final String TAG_VIRTUALSIZE ="VirtualSize";


    //API ENDPOINTS
    private static final String API_LIST_IMAGES = "/images/json";
    //Private static final String API_LIST_CONTRAINERS = "/containers/json";
    private static final String API_LIST_INFO = "/info";


    /*******************************************
     *
     * Class mathods definition
     *
     * ******************************************/


    //Constructor
    public DockerEntity(String host, String portNum) {
        try {
            connectionHost += host + ":" + portNum;
                if (connectionHost == "http://:") {
                throw new CustomException("Empty connection info ....");
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    //API call to docker
    public ArrayList<HashMap<String, String>> getDockerImages (String host) {
        String url = host+API_LIST_IMAGES;
        //Initialize API requests
        GenericRequest sendReq  =   new GenericRequest();
        String jsonResponse = sendReq.makeWebCall(url,GenericRequest.GETR);
        Log.d("Response: ", "> " + jsonResponse);
        imagesList = ParseJSON(jsonResponse,DOCKER_IMAGES);

        return imagesList;
    }

    //copy
    public ArrayList<HashMap<String, String>> getDockerInfo (String host) {
        String url = host+API_LIST_INFO;
        //Initialize API requests
        GenericRequest sendReq  =   new GenericRequest();
        String jsonResponse = sendReq.makeWebCall(url,GenericRequest.GETR);
        Log.d("Response: ", "> " + jsonResponse);
        dockerInfo = ParseJSON(jsonResponse,DOCKER_INFO);

        return dockerInfo;
    }

    //paste
    public boolean checkConnection (String host) {
        String url = host+API_LIST_INFO;
        //Initialize API requests
        GenericRequest sendReq  =   new GenericRequest();
        String jsonResponse = sendReq.makeWebCall(url,GenericRequest.GETR);
        Log.d("Response: ", "> " + jsonResponse);
        return !(jsonResponse == "");
    }

    //Convert to understandable date
    private String convertUnixDate (long unixSeconds){
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    //Docked init exception
    class CustomException extends Exception
    {
        public CustomException (String message)
        {
            super(message);
        }
    }

    //Parsing JSON DATA
    private ArrayList<HashMap<String, String>> ParseJSON(String json, int objectToParse) {
        if (json != null) {
            try {
                // Hashmap for new View
                ArrayList<HashMap<String, String>> nodesList = new ArrayList<HashMap<String, String>>();

                if (objectToParse == DOCKER_INFO){
                    //Docker Info response has only 1 JSONObject.
                    JSONObject info = new  JSONObject(json);
                    String name = info.getString(TAG_NAME);
                    String os = info.getString(TAG_OS);
                    String kernelVer = info.getString(TAG_KERNEL_VERSION);
                    String contCount = info.getString(TAG_CONTAINERS_COUNT);
                    String imgCount = info.getString(TAG_IMAGES_COUNT);

                    // tmp hashmap for single node
                    HashMap<String, String> node = new HashMap<String, String>();

                    //Add values to node. Will be only 1 node.
                    node.put(TAG_NAME, name);
                    node.put(TAG_OS, os);
                    node.put(TAG_KERNEL_VERSION, kernelVer);
                    node.put(TAG_CONTAINERS_COUNT, contCount);
                    node.put(TAG_IMAGES_COUNT, imgCount);
                    nodesList.add(node);

                }else {
                    // Getting JSON Array node
                    JSONArray nodes = new JSONArray(json);

                    // looping through All Students
                    for (int i = 0; i < nodes.length(); i++) {
                        JSONObject c = nodes.getJSONObject(i);

                        // tmp hashmap for single node
                        HashMap<String, String> node = new HashMap<String, String>();

                        //select what we are loking for
                        switch (objectToParse) {
                            case 1:
                                //Parsing Images
                                String id = c.getString(TAG_ID);
                                String name = c.getString(TAG_REPOTAGS);
                                String imageDate = c.getString(TAG_CREATED);
                                imageDate = convertUnixDate(Long.parseLong(imageDate, 10));
                                String size = c.getString(TAG_VIRTUALSIZE);

                                // adding each child node to HashMap key => value
                                // node.put(TAG_ID, id); will be needed for future actions
                                node.put(TAG_REPOTAGS, name);
                                node.put(TAG_CREATED, imageDate);
                                node.put(TAG_VIRTUALSIZE, size);
                                break;

                            case 2:
                                // Space for containers and future parsing
                                break;

                            default:

                                //System.out.println("Invalid.");
                                break;
                        }
                        // adding parsed object to the list
                        nodesList.add(node);
                    }

                }
                return nodesList;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the JSON");
            return null;
        }
    }

}
