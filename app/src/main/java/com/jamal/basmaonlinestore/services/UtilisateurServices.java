package com.jamal.basmaonlinestore.services;


import com.google.gson.JsonObject;
import com.jamal.basmaonlinestore.beans.Utilisateur;
import com.jamal.basmaonlinestore.beans.UtilisateurConnection;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface UtilisateurServices {

    @POST("login")
    Call<Void> login(@Body UtilisateurConnection utilisateurConnection);

    @POST("utilisateurs")
    Call<ResponseBody> inscription(@Body Utilisateur utilisateur);

    @GET("listUser")
    Call<List<Utilisateur>> getListUser(@Header("Authorization") String Authorization);

    @PATCH("activeUser")
    Call<ResponseBody> activeUser(@Header("Authorization") String Authorization, @Body JsonObject body);

}
