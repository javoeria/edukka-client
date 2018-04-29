package com.javier.edukka.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.javier.edukka.R;
import com.javier.edukka.controller.UserSingleton;
import com.javier.edukka.model.UserModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;
import com.javier.edukka.service.HelperClient;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton fab;
    private TextView name, role, score, score_title;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        name = (TextView) findViewById(R.id.user_name);
        role = (TextView) findViewById(R.id.user_role);
        userImage = (ImageView) findViewById(R.id.image);
        score = (TextView) findViewById(R.id.user_score);
        score_title = (TextView) findViewById(R.id.score_title);
        loadJSON();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                Integer id = Integer.parseInt(UserSingleton.getInstance().getUserModel().getId());
                intent.putExtra(ProfileEditActivity.EXTRA_POSITION, id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    protected void onRestart() {
        super.onRestart();
        loadJSON();
    }

    private void loadJSON(){
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<UserModel> call = restInterface.getUser(position);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                UserModel jsonResponse = response.body();
                collapsingToolbar.setTitle(jsonResponse.getUsername());
                name.setText(jsonResponse.getName());
                if (jsonResponse.getRole().equals("student")) {
                    score.setText(getString(R.string.user_score, jsonResponse.getScore()));
                } else {
                    score_title.setVisibility(View.INVISIBLE);
                    score.setVisibility(View.INVISIBLE);
                }

                if (Locale.getDefault().getLanguage().equals("es")) {
                    String rol = HelperClient.roleTranslateEs(jsonResponse.getRole());
                    role.setText(rol);
                } else {
                    String upperString = jsonResponse.getRole().substring(0,1).toUpperCase() + jsonResponse.getRole().substring(1);
                    role.setText(upperString);
                }

                int resourceId = getResources().getIdentifier(jsonResponse.getImage(), "drawable", getPackageName());
                userImage.setImageDrawable(getResources().getDrawable(resourceId));
                if (UserSingleton.getInstance().getUserModel().getUsername().equals(jsonResponse.getUsername())) {
                    fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }
}
