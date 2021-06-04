package com.jamal.basmaonlinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.jamal.basmaonlinestore.beans.GlobalConf;
import com.jamal.basmaonlinestore.beans.UtilisateurConnect;
import com.jamal.basmaonlinestore.beans.UtilisateurConnection;
import com.jamal.basmaonlinestore.security.JWTUtils;
import com.jamal.basmaonlinestore.services.UtilisateurServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonConnection = (Button) findViewById(R.id.buttonConnection);
        Button buttonInscription = (Button) findViewById(R.id.buttonInscription);
        buttonConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _login();
            }
        });
        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _inscription();
            }
        });
    }

    private void _login() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(GlobalConf.nomServer)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        UtilisateurServices utilisateurServices = retrofit.create(UtilisateurServices.class);

        String username = ((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();
        Call<Void> call = utilisateurServices.login(new UtilisateurConnection(username,password));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                try {
                    TextView textView = (TextView) findViewById(R.id.textViewError);
                    String token=" "+response.headers().get("Authorization");
                    if(!token.equals(" null")){
                        textView.setVisibility(View.INVISIBLE);
                        SharedPreferences sharedpreferences = getSharedPreferences(GlobalConf.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Token", "basma" + token);
                        String decodeToken = JWTUtils.decoded(token);
                        editor.putString("UtilisateurConnect", decodeToken);
                        editor.commit();
                        Gson gson = new Gson();
                        UtilisateurConnect obj = gson.fromJson(decodeToken, UtilisateurConnect.class);
                        Intent intent;
                        if (obj.getRoles().indexOf("ADMIN") == -1){
                            intent = new Intent(MainActivity.this, UserActivity.class);
                        } else {
                            intent = new Intent(MainActivity.this, ListUtilisateurActivity.class);
                        }
                        startActivity(intent);
                    } else {
                        textView.setText("username ou mot de pass et incorect");
                        textView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void _inscription() {
        Intent intent = new Intent(this, InscriptionActivity.class);
        startActivity(intent);
    }
}