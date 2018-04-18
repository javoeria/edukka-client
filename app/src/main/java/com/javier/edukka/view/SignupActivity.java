package com.javier.edukka.view;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.javier.edukka.R;
import com.javier.edukka.adapter.AvatarAdapter;
import com.javier.edukka.controller.UserSingleton;
import com.javier.edukka.model.UserModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private final String[] array = {"avatar_butterfly", "avatar_cat", "avatar_dog", "avatar_elephant",
            "avatar_lion", "avatar_panda", "avatar_snake", "avatar_spider", "avatar_turtle", "avatar_wolf"};
    private AvatarAdapter avatarAdapter;
    private List<Drawable> avatars;
    private EditText name, user, pass, classid;
    private String radio = "Student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        StatusBarUtil.setTransparent(this);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.fullname);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        classid = findViewById(R.id.classid);

        initAvatars();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        avatarAdapter = new AvatarAdapter(avatars);
        recyclerView.setAdapter(avatarAdapter);

        CardView cardview = findViewById(R.id.cardView);
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFieldValidation()) {
                    setContentView(R.layout.progressbar_layout);
                    signup();
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_student:
                if (checked)
                    radio = "Student";
                break;
            case R.id.radio_teacher:
                if (checked)
                    radio = "Teacher";
                break;
        }
    }

    private boolean checkFieldValidation() {
        boolean valid = true;
        if (name.getText().toString().equals("")) {
            name.setError("Can't be Empty");
            valid = false;
        } else if (user.getText().toString().equals("")) {
            user.setError("Can't be Empty");
            valid = false;
        } else if (pass.getText().toString().equals("")) {
            pass.setError("Can't be Empty");
            valid = false;
        }
        return valid;
    }

    private void initAvatars() {
        avatars = new ArrayList<>();
        TypedArray a = getResources().obtainTypedArray(R.array.avatar_pictures);
        for (int i=0; i<array.length; i++) {
            avatars.add(a.getDrawable(i));
        }
        a.recycle();
    }

    private void signup() {
        if (classid.getText().toString().equals("")) {
            classid.setText("0");
        }
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<UserModel> request = restInterface.signUp(name.getText().toString(), user.getText().toString(), pass.getText().toString(),
                radio, array[avatarAdapter.getSelectedPos()], Integer.parseInt(classid.getText().toString()));
        request.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                finish();
                UserModel model = response.body();
                if (model.getId()==null) {
                    Toast.makeText(SignupActivity.this, "Invalid UserName/Pass", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                } else {
                    UserSingleton.getInstance().setUserModel(model);
                    Toast.makeText(SignupActivity.this, "Login In SuccessFully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                finish();
                startActivity(getIntent());
                Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("error", call.toString());
            }
        });
    }

}
