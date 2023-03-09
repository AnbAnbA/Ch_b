package com.example.ch_b;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {

    final static String userVariableKey = "USER_VARIABLE";

    private AdapterMaskQuote pAdapter;
    private List<MaskQuote> listQuote = new ArrayList<>();

    private AdapterMaskFeeling dataRVAdapter;
    private List<MaskFeeling> listFeeling = new ArrayList<>();

    ImageView imageProfile;
    TextView textHello;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView ivProducts = findViewById(R.id.lvQuotes);
        pAdapter = new AdapterMaskQuote(MainActivity.this, listQuote);
        ivProducts.setAdapter(pAdapter);
        new GetQuotes().execute();

        RecyclerView rvFeeling = findViewById(R.id.recyclerView);
        rvFeeling.setHasFixedSize(true);
        rvFeeling.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        dataRVAdapter = new AdapterMaskFeeling(listFeeling, MainActivity.this);
        rvFeeling.setAdapter(dataRVAdapter);
        new GetFeeling().execute();

        imageProfile = findViewById(R.id.ivProfile);
        new AdapterMaskQuote.DownloadImageTask((ImageView) imageProfile)
                .execute(Login.User.getAvatar());

        textHello = findViewById(R.id.hello);
        textHello.setText(textHello.getText().toString() + Login.User.getNickName() + "!");


    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(userVariableKey, "fgfggf");
        //outState.putSerializable(userVariableKey, (Serializable) Login.User);
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // получаем объект User в переменную
        //Login.User = (MaskUser) savedInstanceState.getSerializable(userVariableKey);
        String str = savedInstanceState.getString(userVariableKey);
        textHello.setText(str);
    }

    private class GetQuotes extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://mskko2021.mad.hakta.pro/api/quotes");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null)
                {
                    result.append(line);
                }
                return result.toString();
            }
            catch (Exception exception)
            {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                listQuote.clear();
                pAdapter.notifyDataSetInvalidated();

                JSONObject object = new JSONObject(s);
                JSONArray tempArray  = object.getJSONArray("data");

                for (int i = 0;i<tempArray.length();i++)
                {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    MaskQuote tempProduct = new MaskQuote(
                            productJson.getInt("id"),
                            productJson.getString("title"),
                            productJson.getString("image"),
                            productJson.getString("description")
                    );
                    listQuote.add(tempProduct);
                    pAdapter.notifyDataSetInvalidated();
                }

            }
            catch (Exception exception)
            {
                Toast.makeText(MainActivity.this, "При выводе данных возникла ошибка", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetFeeling extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://mskko2021.mad.hakta.pro/api/feelings");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null)
                {
                    result.append(line);
                }
                return result.toString();
            }
            catch (Exception exception)
            {
                return null;
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                listFeeling.clear();
                dataRVAdapter.notifyDataSetChanged();

                JSONObject object = new JSONObject(s);
                JSONArray tempArray  = object.getJSONArray("data");

                for (int i = 0;i<tempArray.length();i++)
                {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    MaskFeeling tempProduct = new MaskFeeling(
                            productJson.getInt("id"),
                            productJson.getString("title"),
                            productJson.getString("image"),
                            productJson.getInt("position")
                    );
                    listFeeling.add(tempProduct);
                    dataRVAdapter.notifyDataSetChanged();
                }
                listFeeling.sort(Comparator.comparing(MaskFeeling::getPosition));
                dataRVAdapter.notifyDataSetChanged();
            }
            catch (Exception exception)
            {
                Toast.makeText(MainActivity.this, "При выводе данных возникла ошибка", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public  void nextMenu(View view)
    {
        startActivity(new Intent(this, Menu.class));
    }

    public  void nextProfile(View view)
    {
        startActivity(new Intent(this, Profile.class));
    }

    public void nextListen(View view)
    {
        startActivity(new Intent(this, Listen.class));
    }
}