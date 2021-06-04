package com.jamal.basmaonlinestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jamal.basmaonlinestore.beans.GlobalConf;
import com.jamal.basmaonlinestore.beans.UtilisateurConnect;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        SharedPreferences sharedpreferences = getSharedPreferences(GlobalConf.MyPREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        UtilisateurConnect obj = gson.fromJson(sharedpreferences.getString("UtilisateurConnect",null), UtilisateurConnect.class);
        ((TextView) findViewById(R.id.textViewUser)).setText("Bonjour : " + obj.getFirstName() + " " + obj.getLastName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.deconnexion_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedpreferences = getSharedPreferences(GlobalConf.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}