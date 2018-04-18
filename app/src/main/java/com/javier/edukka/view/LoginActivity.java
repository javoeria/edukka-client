package com.javier.edukka.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.javier.edukka.R;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;
import com.javier.edukka.controller.UserSingleton;
import com.javier.edukka.model.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setTransparent(this);

        user = findViewById(R.id.editText);
        pass = findViewById(R.id.editText2);
        CardView cardview = findViewById(R.id.cardView);
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckFieldValidation()) {
                    setContentView(R.layout.progressbar_layout);
                    login();
                }
            }
        });
    }

    private boolean CheckFieldValidation() {
        boolean valid = true;
        if (user.getText().toString().equals("")) {
            user.setError("Can't be Empty");
            valid = false;
        } else if (pass.getText().toString().equals("")) {
            pass.setError("Can't be Empty");
            valid = false;
        }
        //return valid;
        return true;
    }

    private void login() {
        RestInterface restInterface = RetrofitClient.getInstance();
        //Call<UserModel> request = restInterface.logIn(user.getText().toString(), pass.getText().toString());
        Call<UserModel> request = restInterface.logIn("javi", "123");
        request.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                finish();
                UserModel model = response.body();
                if (model.getId()==null) {
                    Toast.makeText(LoginActivity.this, "Invalid UserName/Pass", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                } else {
                    UserSingleton.getInstance().setUserModel(model);
                    Toast.makeText(LoginActivity.this, "Login In SuccessFully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                finish();
                startActivity(getIntent());
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClick(View v) {
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(i);
    }
}
