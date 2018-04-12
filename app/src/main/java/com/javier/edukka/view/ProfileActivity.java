package com.javier.edukka.view;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.javier.edukka.R;
import com.javier.edukka.model.UserModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView name, score;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        name = (TextView) findViewById(R.id.user_name);
        score = (TextView) findViewById(R.id.user_score);
        userImage = (ImageView) findViewById(R.id.image);
        loadJSON();
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
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel jsonResponse = response.body();
                collapsingToolbar.setTitle(jsonResponse.getName());
                name.setText(jsonResponse.getName()+' '+jsonResponse.getSurname());
                score.setText(jsonResponse.getScore()+" pts");
                userImage.setImageResource(R.drawable.dog);
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }
}
