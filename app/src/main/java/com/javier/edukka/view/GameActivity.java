package com.javier.edukka.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.javier.edukka.R;
import com.javier.edukka.model.GameModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_SUBJECT = "subject";
    private Map<String, String> map;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView detail, difficulty, vote;
    private ImageView subjectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        detail = findViewById(R.id.game_detail);
        difficulty = findViewById(R.id.game_difficulty);
        vote = findViewById(R.id.game_vote);
        subjectImage = findViewById(R.id.image);
        initMap();
        loadJSON();
    }

    private void initMap() {
        map = new HashMap<>();
        map.put("Spanish Language", "subject_spanish");
        map.put("Mathematics", "subject_math");
        map.put("Natural Sciences", "subject_natural");
        map.put("Social Sciences", "subject_social");
        map.put("Biology & Geology", "subject_biology");
        map.put("Geography & History", "subject_geography");
        map.put("Music", "subject_music");
        map.put("Sports", "subject_sport");
        map.put("English Language", "subject_english");
        map.put("General Knowledge", "subject_general");
    }

    private void loadJSON(){
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<GameModel> call = restInterface.getGame(position);
        call.enqueue(new Callback<GameModel>() {
            @Override
            public void onResponse(@NonNull Call<GameModel> call, @NonNull Response<GameModel> response) {
                GameModel jsonResponse = response.body();
                collapsingToolbar.setTitle(jsonResponse.getTitle());
                detail.setText(jsonResponse.getDescription());
                difficulty.setText(jsonResponse.getDifficulty());
                if (Integer.parseInt(jsonResponse.getVote()) > 0) {
                    vote.setText("+"+jsonResponse.getVote());
                    vote.setTextColor(Color.GREEN);
                } else if (Integer.parseInt(jsonResponse.getVote()) < 0) {
                    vote.setText(jsonResponse.getVote());
                    vote.setTextColor(Color.RED);
                }
                String s = getIntent().getStringExtra(EXTRA_SUBJECT);
                int resourceId = getResources().getIdentifier(map.get(s), "drawable", getPackageName());
                subjectImage.setImageDrawable(getResources().getDrawable(resourceId));
            }

            @Override
            public void onFailure(@NonNull Call<GameModel> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
