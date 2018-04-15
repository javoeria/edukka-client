package com.javier.edukka.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.javier.edukka.R;
import com.javier.edukka.adapter.GameAdapter;
import com.javier.edukka.model.GameModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    private TextView detail, difficulty, vote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);

        detail = (TextView) findViewById(R.id.game_detail);
        difficulty = (TextView) findViewById(R.id.game_difficulty);
        vote = (TextView) findViewById(R.id.game_vote);
        loadJSON(position);
    }

    private void loadJSON(int id){
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<GameModel> call = restInterface.getGame(id);
        call.enqueue(new Callback<GameModel>() {
            @Override
            public void onResponse(Call<GameModel> call, Response<GameModel> response) {
                GameModel jsonResponse = response.body();
                getSupportActionBar().setTitle(jsonResponse.getTitle());
                detail.setText(jsonResponse.getDescription());
                difficulty.setText(jsonResponse.getDifficulty());
                if (Integer.parseInt(jsonResponse.getVote()) > 0) {
                    vote.setText("+"+jsonResponse.getVote());
                    vote.setTextColor(Color.GREEN);
                } else if (Integer.parseInt(jsonResponse.getVote()) < 0) {
                    vote.setText(jsonResponse.getVote());
                    vote.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFailure(Call<GameModel> call, Throwable t) {
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
