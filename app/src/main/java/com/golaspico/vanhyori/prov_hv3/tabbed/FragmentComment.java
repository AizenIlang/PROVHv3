package com.golaspico.vanhyori.prov_hv3.tabbed;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.golaspico.vanhyori.prov_hv3.CommentSectionAdapter;
import com.golaspico.vanhyori.prov_hv3.Lobby;
import com.golaspico.vanhyori.prov_hv3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import modules.Comments;
import modules.User;


public class FragmentComment extends Fragment{

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser firebaseUser;
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private Button mSendButton;
    private RatingBar mRating;

    private String Key;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment,container,false);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mRecyclerView = view.findViewById(R.id.fragment_comment_recyclerview);
        mEditText = view.findViewById(R.id.fragment_comment_comment);
        mSendButton = view.findViewById(R.id.fragment_comment_sendbutton);
        mRating = view.findViewById(R.id.fragment_comment_rating);

        Key = getArguments().getString("Key");
//
//        Log.e("The Key :" ,Key);



        CommentSectionAdapter commentSectionAdapter = new CommentSectionAdapter(getContext(),Key);
        mRecyclerView.setAdapter(commentSectionAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mEditText.getText().toString().isEmpty()){
                    AddComment(mEditText.getText().toString(),mRating.getRating());
                }else{
                    Toast.makeText(getContext(),"Message cannot be empty",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;

    }

    private void AddComment(String comment, float Rating){



        final Comments comments = new Comments();
        comments.setUid(firebaseUser.getUid());
        comments.setApproved(true);
        comments.setRate(Rating);
        comments.setMessage(comment);
        databaseReference.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User myUser = dataSnapshot.getValue(User.class);
                comments.setName(myUser.getFirstName() + " " + myUser.getLastName());
                databaseReference.child("Hospitals").child(Key).child("Comments").child(firebaseUser.getUid()).setValue(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


////        Log.e("After Database push : ", Lobby.KEY);


    }
}
