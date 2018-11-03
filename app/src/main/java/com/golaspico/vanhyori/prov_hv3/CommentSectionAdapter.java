package com.golaspico.vanhyori.prov_hv3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import modules.Comments;


public class CommentSectionAdapter extends RecyclerView.Adapter<CommentSectionAdapter.CommentSectionViewHolder> {

    private TextView Message;
    private TextView Name;
    private RatingBar Rating;

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Comments> commentsArrayList;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String Path;


    public CommentSectionAdapter(Context context, String Key) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        commentsArrayList = new ArrayList<Comments>();
        Path = Key;



        databaseReference.child("Hospitals").child(Path).child("Comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Comments tempComments = dataSnapshot.getValue(Comments.class);

                AddComments(tempComments);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @NonNull
    @Override
    public CommentSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comment_list_item,parent,false);
        CommentSectionViewHolder holder = new CommentSectionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentSectionViewHolder holder, int position) {
        Comments comments = commentsArrayList.get(position);

        Message.setText(comments.getMessage());
        Name.setText(comments.getName());
        Rating.setRating(comments.getRate());

    }

    @Override
    public int getItemCount() {
        return commentsArrayList.size();
    }


    public void AddComments(Comments comments){

        int endofList=0;
        if(commentsArrayList.size() > 0){
            endofList = commentsArrayList.size() - 1;
        }
        commentsArrayList.add(endofList,comments);
        //Todo double check the ID by itterating then notifydataposition changed
        notifyItemInserted(0);


    }

    public class CommentSectionViewHolder extends RecyclerView.ViewHolder{

        public CommentSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            Message = itemView.findViewById(R.id.comment_list_item_message);
            Name = itemView.findViewById(R.id.comment_list_item_name);
            Rating = itemView.findViewById(R.id.comment_list_item_rating);

        }
    }
}
