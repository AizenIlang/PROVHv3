package com.golaspico.vanhyori.prov_hv3;

import android.support.design.bottomappbar.BottomAppBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Lobby extends AppCompatActivity {

    RecyclerView hospitalRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        BottomAppBar bottomAppBar = findViewById(R.id.lobby_bottom_appbar);
        setSupportActionBar(bottomAppBar);

        hospitalRecyclerView = findViewById(R.id.lobby_recycler_view);
        hospitalRecyclerView.setAdapter(new HospitalAdapter(this));

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
        if(item.getItemId() == R.id.lobby_menu_show_nearest){
            Toast.makeText(getApplicationContext(),"Getting Nearest Hospital",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
