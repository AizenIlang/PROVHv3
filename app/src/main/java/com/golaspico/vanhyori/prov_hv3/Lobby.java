package com.golaspico.vanhyori.prov_hv3;

import android.content.Intent;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Lobby extends AppCompatActivity {



    RecyclerView hospitalRecyclerView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FloatingActionButton floatingActionButton;

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

        floatingActionButton = findViewById(R.id.lobby_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Appointments.class);
                startActivity(intent);
            }
        });

        hospitalRecyclerView = findViewById(R.id.lobby_recycler_view);
        hospitalRecyclerView.setAdapter(new HospitalAdapter(this,this));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hospitalRecyclerView.setLayoutManager(layoutManager);

        hospitalRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lobby_bar_menu,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.lobby_menu_show_nearest){
//            Toast.makeText(getApplicationContext(),"Getting Nearest Hospital",Toast.LENGTH_SHORT).show();
//        }

        if(item.getItemId() == R.id.lobby_menu_user){
            Intent intent = new Intent(this,UserProfile.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.lobby_menu_map_nearest){
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
