package com.example.android.justjava.connection;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.justjava.R;
import com.example.android.justjava.main.SessionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class imageAdapter extends  RecyclerView.Adapter<imageAdapter.ViewHolder> {

    // / Store a member variable for the contacts
    private ArrayList<HashMap<String,String>> mImg=(ArrayList<HashMap<String,String>>) SessionActivity.ama;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView imageName;
        public TextView imageId;
        public TextView imageDate;
        public TextView imageVirtSize;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            imageName = (TextView) itemView.findViewById(R.id.image_name);
            imageId = (TextView) itemView.findViewById(R.id.img_id);
            imageDate = (TextView) itemView.findViewById(R.id.date_holder_val);
            imageVirtSize = (TextView) itemView.findViewById(R.id.vurts_holder_val);

        }
    }


    // Pass in the image array into the constructor
    public imageAdapter(ArrayList<HashMap<String,String>> images) {
        mImg =(ArrayList<HashMap<String,String>>) images;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public imageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View imageView = inflater.inflate(R.layout.item_image, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(imageView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(imageAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        HashMap<String, String> image = mImg.get(position);

        // Set item views based on the data model
        TextView imageName = viewHolder.imageName;
        imageName.setText(image.get(DockerEntity.TAG_REPOTAGS));

        TextView imageId = viewHolder.imageId;
        imageId.setText(image.get(DockerEntity.TAG_ID));

        TextView imageDate = viewHolder.imageDate;
        imageDate.setText(image.get(DockerEntity.TAG_CREATED));

        TextView imageVirtSize = viewHolder.imageVirtSize;
        imageVirtSize.setText(image.get(DockerEntity.TAG_VIRTUALSIZE));

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mImg.size();
    }

}
