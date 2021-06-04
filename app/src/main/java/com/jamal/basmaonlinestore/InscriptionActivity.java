package com.jamal.basmaonlinestore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jamal.basmaonlinestore.beans.GlobalConf;
import com.jamal.basmaonlinestore.beans.Utilisateur;
import com.jamal.basmaonlinestore.services.UtilisateurServices;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        Button buttonConnection = (Button) findViewById(R.id.buttonConnection1);
        Button buttonInscription = (Button) findViewById(R.id.buttonInscription1);
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

    private void _login(){
        super.onBackPressed();
    }

    public void _inscription(){
        String firstName = ((EditText) findViewById(R.id.editTextInscriptionFirstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.editTextInscriptionLastName)).getText().toString();
        String username = ((EditText) findViewById(R.id.editTextInscriptionUserName)).getText().toString();
        String email = ((EditText) findViewById(R.id.editTextInscriptionEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextInscriptionPassword)).getText().toString();

        TextView text_error = (TextView) findViewById(R.id.textViewErrorInscription);

        if (matchRegex("^[A-Za-z ]{3,}$", lastName)) {
            if (matchRegex("^[A-Za-z ]{3,}$", firstName)) {
                if (matchRegex("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", email)) {
                    if(matchRegex("^[A-Za-z ]{3,}$", username)) {
                        if(matchRegex("^[A-Za-z0-9@$!%*#?&]{8,}$", password)) {
                            text_error.setText("");
                            Retrofit.Builder builder = new Retrofit.Builder()
                                    .baseUrl(GlobalConf.nomServer)
                                    .addConverterFactory(GsonConverterFactory.create());
                            Retrofit retrofit = builder.build();
                            UtilisateurServices utilisateurServices = retrofit.create(UtilisateurServices.class);
                            Call<ResponseBody> call = utilisateurServices.inscription(new Utilisateur(null, firstName, lastName, username, email, password));
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.isSuccessful()){
                                        try {
                                            Toast.makeText(InscriptionActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                                            _login();
                                        } catch (IOException e) {
                                            Toast.makeText(InscriptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        try {
                                            Toast.makeText(InscriptionActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            Toast.makeText(InscriptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(InscriptionActivity.this, "tttttt"+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            text_error.setText("password est invalid");
                        }
                    } else {
                        text_error.setText("username est invalid");
                    }
                } else {
                    text_error.setText("email est invalid");
                }
            } else {
                text_error.setText("prenom est invalid");
            }
        } else {
            text_error.setText("nom est invalid");
        }
    }

    public boolean matchRegex(String regex, String text){
        Pattern VALID_Match_ADDRESS_REGEX = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_Match_ADDRESS_REGEX.matcher(text);
        return matcher.find();
    }
}