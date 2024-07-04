package com.example.photoverseapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private Context context;
    private List<Photoverse> photolists;


    public MyAdapter(Context context, List<Photoverse> photolists) {
        this.context = context;
        this.photolists = photolists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.photo_row,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Photoverse currentphotoverse = photolists.get(position);

        holder.title.setText(currentphotoverse.getTitle());
        holder.description.setText(currentphotoverse.getThoughts());
        holder.name.setText(currentphotoverse.getUsername());

        String imageurl = currentphotoverse.getImageUrl();
        String timemark = (String) DateUtils.getRelativeTimeSpanString(
                currentphotoverse.getTimeAdded().getSeconds()*1000
        );
        holder.dateAdded.setText(timemark);

        Glide.with(context).load(imageurl).fitCenter().into(holder.image);

//
        holder.share.setOnClickListener(v -> {
            // Assuming imageurl is a Firebase Storage URL, you can directly use it to share
            String imagesurl = currentphotoverse.getImageUrl();
            Uri imageUri = Uri.parse(imagesurl);

            // Create the share Intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("post_images/*"); // Set MIME type to image/*
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri); // Add the image URI as an extra stream

            // Create chooser Intent to show the list of available sharing options
            Intent chooserIntent = Intent.createChooser(shareIntent, "Share Image via");

            // Start the chooser activity
            context.startActivity(chooserIntent);
        });
//
    }



    @Override
    public int getItemCount() {
        return photolists.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title,description,dateAdded,name;
        public ImageView image,share;
        public String userid,username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title_list);
            description=itemView.findViewById(R.id.Description_list);
            dateAdded=itemView.findViewById(R.id.timestamp_list);
            image=itemView.findViewById(R.id.image_list);
            name=itemView.findViewById(R.id.row_username);
            share=itemView.findViewById(R.id.share_button);

//            share.setOnClickListener(v -> {
//                // Assuming you have the URI of the image you want to share
//
//                Uri imageUri = Uri.parse();
//
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("image/*"); // Set MIME type to image/* to indicate you're sharing an image
//                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri); // Add the image URI as an extra stream
//                Intent chooserIntent = Intent.createChooser(shareIntent, "Share Image via");
//
//            });
        }
    }
}
