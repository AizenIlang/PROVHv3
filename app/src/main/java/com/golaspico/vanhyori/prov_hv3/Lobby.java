package com.golaspico.vanhyori.prov_hv3;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import modules.Hospital;

public class Lobby extends AppCompatActivity {



    RecyclerView hospitalRecyclerView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FloatingActionButton floatingActionButton;
    Button mSearchBtn;
    Button mSpeechBtn;
    EditText mSearchEdit;
    Activity myActivity;
    HospitalAdapter hospitalAdapter;
    Spinner spinner;
    ArrayList<Hospital> hospitalArrayList;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser temp =mAuth.getCurrentUser();
        if(temp == null){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        BottomAppBar bottomAppBar = findViewById(R.id.lobby_bottom_appbar);
        setSupportActionBar(bottomAppBar);

        myActivity = this;
        floatingActionButton = findViewById(R.id.lobby_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Appointments.class);
                startActivity(intent);
            }
        });

        hospitalRecyclerView = findViewById(R.id.lobby_recycler_view);





        hospitalAdapter = new HospitalAdapter(this,this,"","");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hospitalRecyclerView.setLayoutManager(layoutManager);

        hospitalRecyclerView.setItemAnimator(new DefaultItemAnimator());

        hospitalRecyclerView.setAdapter(hospitalAdapter);



        final Spinner spinner = (Spinner) findViewById(R.id.lobby_spinner_category);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        mSearchEdit = findViewById(R.id.lobby_search_edit_text);

        mSearchBtn = findViewById(R.id.lobby_search_btn);

        mSpeechBtn = findViewById(R.id.lobby_speech);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mSearchEdit.getText().toString().isEmpty()){
                   hospitalAdapter.UpdateList(mSearchEdit.getText().toString(),spinner.getSelectedItem().toString());
                   hospitalAdapter.notifyDataSetChanged();
                }else{

                }
            }
        });


        mSpeechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpeechInput();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lobby_bar_menu,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.lobby_menu_map){
            createNotificationChannel();
        }

        if(item.getItemId() == R.id.lobby_menu_user){
            Intent intent = new Intent(this,UserProfile.class);
            startActivityForResult(intent,10);
        }

        if(item.getItemId() == R.id.lobby_menu_map_nearest){
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void getSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,10);
        }else{
            Toast.makeText(this,"Your Device is not supported",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10 :
                if(data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mSearchEdit.setText(result.get(0));

                }

                break;
        }
    }

    private void createNotificationChannel() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"Hwoarang")
                .setSmallIcon(R.drawable.ic_assignment_black_24dp)
                .setContentTitle("PROV-H")
                .setContentText("Nov 30, 8:00am Alfonso Specialist Hospital")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("PROV-H"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());
    }
}
