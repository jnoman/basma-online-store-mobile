package com.jamal.basmaonlinestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.jamal.basmaonlinestore.beans.GlobalConf;
import com.jamal.basmaonlinestore.beans.Utilisateur;
import com.jamal.basmaonlinestore.beans.UtilisateurConnection;
import com.jamal.basmaonlinestore.services.UtilisateurServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListUtilisateurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_utilisateur);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(GlobalConf.nomServer)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        UtilisateurServices utilisateurServices = retrofit.create(UtilisateurServices.class);
        SharedPreferences sharedpreferences = getSharedPreferences(GlobalConf.MyPREFERENCES, Context.MODE_PRIVATE);
        Call<List<Utilisateur>> call = utilisateurServices.getListUser(sharedpreferences.getString("Token",null));
        call.enqueue(new Callback<List<Utilisateur>>() {
            @Override
            public void onResponse(Call<List<Utilisateur>> call, Response<List<Utilisateur>> response) {
                if (response.isSuccessful()){
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ListUtilisateurActivity.this));
                    recyclerView.setHasFixedSize(true);
                    List<Utilisateur> list = response.body();
                    if(list.size()>0){
                        UtilisateuAdapterClass utilisateurAdapterClass = new UtilisateuAdapterClass(list, ListUtilisateurActivity.this, sharedpreferences);
                        recyclerView.setAdapter(utilisateurAdapterClass);
                    } else {
                        Toast.makeText(ListUtilisateurActivity.this, "Aucun Utilisateur dans database", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ListUtilisateurActivity.this, "l'accès est refusé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Utilisateur>> call, Throwable t) {
                Toast.makeText(ListUtilisateurActivity.this, "tttt"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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