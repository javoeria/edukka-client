package com.javier.edukka.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView name, score, role, classid;
    private ImageView userImage;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        name = findViewById(R.id.user_name);
        score = findViewById(R.id.user_score);
        role = findViewById(R.id.user_role);
        classid = findViewById(R.id.user_class);
        userImage = findViewById(R.id.image);
        loadJSON();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
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
                score.setText(jsonResponse.getScore()+" pts");
                role.setText(jsonResponse.getRole());
                classid.setText(jsonResponse.getClassId());
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
