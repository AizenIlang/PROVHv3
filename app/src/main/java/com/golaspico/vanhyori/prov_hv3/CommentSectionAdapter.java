package com.golaspico.vanhyori.prov_hv3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import modules.Comments;


public class CommentSectionAdapter extends RecyclerView.Adapter<CommentSectionAdapter.CommentSectionViewHolder> {



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
                Comments tempComment = dataSnapshot.getValue(Comments.class);
                int theIndex = 0;

                for(Comments comments : commentsArrayList){
                    if(comments.getUid().equals(tempComment.getUid())){
                        theIndex = commentsArrayList.indexOf(comments);
                        commentsArrayList.set(theIndex,tempComment);
                        Log.e("The Comment",comments.getMessage());
                        notifyItemChanged(theIndex);
                    }
                }




            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Comments tempComment = dataSnapshot.getValue(Comments.class);
                int theIndex = 0;

                for(Comments comments : commentsArrayList){
                    if(comments.getUid().equals(tempComment.getUid())){
                        theIndex = commentsArrayList.indexOf(comments);
                    }
                }

                commentsArrayList.remove(theIndex);
                notifyItemRemoved(theIndex);

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
        final Comments comments = commentsArrayList.get(position);

        holder.Message.setText(comments.getMessage());
        holder.Name.setText(comments.getName());
        holder.Rating.setRating(comments.getRate());

        holder.Date.setText(ConvertDate(comments.getDatePostedLong()));
        if(comments.getUid().equals(FirebaseAuth.getInstance().getUid())){
            holder.Close.setVisibility(View.VISIBLE);
            holder.Close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference.child("Hospitals").child(Path).child("Comments").child(comments.getUid()).removeValue();

                }
            });
        }




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
        private TextView Message;
        private TextView Name;
        private RatingBar Rating;
        private TextView Date;
        private ImageView Close;

        public CommentSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            Message = itemView.findViewById(R.id.comment_list_item_message);
            Name = itemView.findViewById(R.id.comment_list_item_name);
            Rating = itemView.findViewById(R.id.comment_list_item_rating);
            Date = itemView.findViewById(R.id.comment_list_item_date);
            Close = itemView.findViewById(R.id.comment_list_item_close);

        }
    }

    public String ConvertDate(long timestamp){

        java.util.Date time=new java.util.Date(timestamp*1000);
        SimpleDateFormat pre = new SimpleDateFormat("EEE MM/dd/yyyy HH:mm:ss");
        return pre.format(time);
    }
}
