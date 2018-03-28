package com.example.fatin.foodbasket;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class PostedImages extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posted_images);
        recyclerView = (RecyclerView)findViewById(R.id.recycleView);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("photos");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Post, PostImage> firebase_adapter = new FirebaseRecyclerAdapter<Post, PostImage>(
                Post.class,
                R.layout.single_post,
                PostImage.class,
                databaseReference) {

            @Override
            protected void populateViewHolder(PostImage viewHolder, Post model, int position) {
                viewHolder.setDescription(model.getDescription());
                viewHolder.setRoom(model.getBuildingName());
                viewHolder.setBuildName(model.getRoomNum());
                viewHolder.setImage(model.getImage());
            }

        };
        recyclerView.setAdapter(firebase_adapter);
    }

    private void deactivateAccount() {
    }

    public static class PostImage extends RecyclerView.ViewHolder{
        View view;

        public PostImage(View _view) {
            super(_view);
            this.view=_view;
        }

        public void setRoom(String room_title) {
            TextView post_title = view.findViewById(R.id.post_room_num);
            post_title.setText("Building Name: "+room_title);
        }
        public void setDescription(String _des) {
            TextView post_title = view.findViewById(R.id.post_description);
            post_title.setText("Description: "+_des);
        }
        public void setBuildName(String build_name) {
            TextView post_title = view.findViewById(R.id.post_building_name);
            post_title.setText("Room#: "+build_name);
        }
        public void setImage(String image){
            ImageView imageView = (ImageView) view.findViewById(R.id.post_image);
            Picasso.get().load(image).into(imageView);
        }
    }
    }



