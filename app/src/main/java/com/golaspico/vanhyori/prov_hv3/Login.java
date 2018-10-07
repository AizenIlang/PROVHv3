package com.golaspico.vanhyori.prov_hv3;

import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Login extends AppCompatActivity {

    ConstraintLayout mLayout;
    AnimationDrawable mDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLayout = findViewById(R.id.login_constraint);
        mDrawable = (AnimationDrawable) mLayout.getBackground();

        mDrawable.setEnterFadeDuration(4500);
        mDrawable.setExitFadeDuration(4500);
        mDrawable.start();
    }
}
