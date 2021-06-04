package com.jamal.basmaonlinestore;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.jamal.basmaonlinestore.beans.GlobalConf;
import com.jamal.basmaonlinestore.beans.Utilisateur;
import com.jamal.basmaonlinestore.services.UtilisateurServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UtilisateuAdapterClass extends RecyclerView.Adapter<UtilisateuAdapterClass.ViewHolder> {

    List<Utilisateur> utilisateurList;
    Context context;
    SharedPreferences sharedpreferences;

    public UtilisateuAdapterClass(List<Utilisateur> utilisateurList, Context context, SharedPreferences sharedpreferences ) {
        this.utilisateurList = utilisateurList;
        this.context = context;
        this.sharedpreferences = sharedpreferences;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.utilisateur_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Utilisateur utilisateur = utilisateurList.get(position);
        holder.textViewUsername.setText(utilisateur.getUsername());
        holder.textViewUserCompletName.setText(utilisateur.getFirstName()+" "+utilisateur.getLastName());
        holder.textViewUserEmail.setText(utilisateur.getEmail());
        if (utilisateur.isActive()){
            holder.activer_button.setText("désactiver compte");
            holder.activer_button.setBackgroundColor(Color.RED);
        }
        boolean active = !utilisateur.isActive();
        holder.activer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilisateurList.get(position).setActive(active);
                utilisateur.setActive(active);
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(GlobalConf.nomServer)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();
                UtilisateurServices utilisateurServices = retrofit.create(UtilisateurServices.class);
                JsonObject paramObject = new JsonObject();

                    paramObject.addProperty("id", utilisateur.getId());
                    paramObject.addProperty("active", active);
                    Log.d("erreuuuuuur1", paramObject.toString());
                    Call<ResponseBody> call = utilisateurServices.activeUser(sharedpreferences.getString("Token",null), paramObject);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                try {
                                    Toast.makeText(context, response.body().string(), Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                    if (active){
                                        holder.activer_button.setText("désactiver compte");
                                        holder.activer_button.setBackgroundColor(Color.RED);
                                    } else {
                                        holder.activer_button.setText("activer compte");
                                        holder.activer_button.setBackgroundColor(Color.rgb(12, 109, 250));
                                    }
                                } catch (IOException e) {
                                    Toast.makeText(context, "bbbbb"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                try {
                                    Log.d("erreuuuuuur", response.errorBody().string());
                                    Toast.makeText(context, "cccc"+response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(context, "dddddd"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, "tttttt"+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


            }
        });

    }

    @Override
    public int getItemCount() {
        return utilisateurList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUsername, textViewUserCompletName, textViewUserEmail;
        Button activer_button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = (TextView) itemView.findViewById(R.id.TextViewUserUsername);
            textViewUserCompletName = (TextView) itemView.findViewById(R.id.TextViewUserCompletName);
            textViewUserEmail = (TextView) itemView.findViewById(R.id.TextViewUserEmail);
            activer_button = (Button) itemView.findViewById(R.id.activer_button);
        }
    }
}
