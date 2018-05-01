package com.javier.edukka.view;

import android.content.Intent;
import android.graphics.Color;
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
import com.javier.edukka.model.GameModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;
import com.javier.edukka.service.HelperClient;

import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_SUBJECT = "subject";
    private final Map<String, String> map = HelperClient.map_subject();
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton fab;
    private TextView detail, difficulty, vote;
    private ImageView subjectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        detail = (TextView) findViewById(R.id.game_detail);
        difficulty = (TextView) findViewById(R.id.game_difficulty);
        vote = (TextView) findViewById(R.id.game_vote);
        subjectImage = (ImageView) findViewById(R.id.image);
        loadJSON();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = getIntent().getIntExtra(EXTRA_POSITION, 0);
                Intent intent = new Intent(GameActivity.this, GameEditActivity.class);
                intent.putExtra(GameEditActivity.EXTRA_POSITION, id);
                startActivity(intent);
            }
        });
    }

    protected void onRestart() {
        super.onRestart();
        loadJSON();
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
                if (Locale.getDefault().getLanguage().equals("es")) {
                    String level = HelperClient.levelTranslateEs(jsonResponse.getDifficulty());
                    difficulty.setText(level);
                } else {
                    String upperString = jsonResponse.getDifficulty().substring(0,1).toUpperCase() + jsonResponse.getDifficulty().substring(1);
                    difficulty.setText(upperString);
                }

                if (Integer.parseInt(jsonResponse.getVote()) > 0) {
                    vote.setText("+"+jsonResponse.getVote());
                    vote.setTextColor(Color.GREEN);
                } else if (Integer.parseInt(jsonResponse.getVote()) < 0) {
                    vote.setText(jsonResponse.getVote());
                    vote.setTextColor(Color.RED);
                } else {
                    vote.setText(jsonResponse.getVote());
                }

                String s = getIntent().getStringExtra(EXTRA_SUBJECT);
                int resourceId = getResources().getIdentifier(map.get(s), "drawable", getPackageName());
                subjectImage.setImageDrawable(getResources().getDrawable(resourceId));
                if (UserSingleton.getInstance().getUserModel().getId().equals(jsonResponse.getTeacherId())) {
                    fab.setVisibility(View.VISIBLE);
                }
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
