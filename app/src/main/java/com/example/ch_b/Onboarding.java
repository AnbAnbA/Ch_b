package com.example.ch_b;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Onboarding extends AppCompatActivity {

    public static String image;
    public static String Name;
    final static String userVariableKey = "USER_VARIABLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        SharedPreferences prefs = this.getSharedPreferences(
                "Date", Context.MODE_PRIVATE);
        if(prefs != null)
        {
            if(!prefs.getString("Name", "").equals(""))
            {
                image = prefs.getString("image", "");
                Name = prefs.getString("Name", "");
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }
    // получение ранее сохраненного состояния


    public void nextRegistrarion(View v)
    {
        startActivity(new Intent(this, Register.class));
    }

    public void nextLogin(View v)
    {
        startActivity(new Intent(this, Login.class));
    }
}