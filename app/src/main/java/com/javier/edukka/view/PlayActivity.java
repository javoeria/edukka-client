package com.javier.edukka.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.javier.edukka.R;
import com.javier.edukka.adapter.PickerAdapter;
import com.javier.edukka.adapter.ScoreAdapter;
import com.javier.edukka.controller.GameSingleton;
import com.javier.edukka.controller.UserSingleton;
import com.javier.edukka.model.ActivityModel;
import com.javier.edukka.model.QuizModel;
import com.javier.edukka.model.UserModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayActivity extends AppCompatActivity {

    private AdapterViewFlipper flipper;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView progressText;
    private ImageView avatarView;
    private FloatingActionButton fab;
    private List<String> questions;
    private List<String> answers;
    private List<String> values;
    private List<String> results;
    private int step = 0;
    private int correct = 0;
    private PickerAdapter pickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(GameSingleton.getInstance().getGameModel().getTitle());

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        avatarView = (ImageView) findViewById(R.id.avatar);
        progressText = (TextView) findViewById(R.id.progress_text);
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        results = new ArrayList<>();
        values = new ArrayList<>();
        loadJSON();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(answers.get(step)) == pickerAdapter.getAnswer()) {
                    results.add("true");
                    correct++;
                } else {
                    results.add("false");
                }

                step++;
                values.add(String.valueOf(pickerAdapter.getAnswer()));
                progressBar.setProgress(progressBar.getProgress()+100/pickerAdapter.getCount());
                progressText.setText((step+1)+"/"+pickerAdapter.getCount());
                flipper.showNext();
                if (step == pickerAdapter.getCount()) {
                    RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                    ratingBar.setRating((correct*5.0f)/questions.size());
                    ratingBar.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.INVISIBLE);

                    ScoreAdapter scoreAdapter = new ScoreAdapter(questions,values,results);
                    recyclerView.setAdapter(scoreAdapter);
                    finishGame();
                }
            }
        });
    }

    private void loadJSON(){
        int id = Integer.parseInt(GameSingleton.getInstance().getGameModel().getId());
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<List<QuizModel>> call = restInterface.getGameQuiz(id);
        call.enqueue(new Callback<List<QuizModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<QuizModel>> call, @NonNull Response<List<QuizModel>> response) {
                List<QuizModel> jsonResponse = response.body();
                for (QuizModel quizModel : jsonResponse) {
                    questions.add(quizModel.getQuestion());
                    answers.add(quizModel.getAnswer());
                }
                progressBar.setProgress(0);
                progressText.setText(1+"/"+questions.size());
                int resourceId = getResources().getIdentifier(UserSingleton.getInstance().getUserModel().getImage(), "drawable", getPackageName());
                avatarView.setImageDrawable(getResources().getDrawable(resourceId));
                flipper = (AdapterViewFlipper) findViewById(R.id.adapter_view);

                pickerAdapter = new PickerAdapter(getApplication(), questions, answers);
                flipper.setAdapter(pickerAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<QuizModel>> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    private void finishGame(){
        int student = Integer.parseInt(UserSingleton.getInstance().getUserModel().getId());
        int game = Integer.parseInt(GameSingleton.getInstance().getGameModel().getId());
        float note = (correct*5.0f)/questions.size();
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<ActivityModel> call = restInterface.finishGame(student, game, GameSingleton.getInstance().getGameModel().getSubject(), note);
        call.enqueue(new Callback<ActivityModel>() {
            @Override
            public void onResponse(@NonNull Call<ActivityModel> call, @NonNull Response<ActivityModel> response) {
                //ActivityModel jsonResponse = response.body();
                Toast.makeText(PlayActivity.this, R.string.data_update, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ActivityModel> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });

        if (note >= 2.5f) {
            String dif = GameSingleton.getInstance().getGameModel().getDifficulty();
            int score = Integer.parseInt(UserSingleton.getInstance().getUserModel().getScore());
            switch (dif) {
                case "easy":
                    score += 10;
                    progressText.setText("+ "+10);
                    break;
                case "medium":
                    score += 20;
                    progressText.setText("+ "+20);
                    break;
                case "hard":
                    score += 30;
                    progressText.setText("+ "+30);
                    break;
            }

            Call<UserModel> call2 = restInterface.updateUserScore(score, student);
            call2.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                    UserModel jsonResponse = response.body();
                    UserSingleton.getInstance().setUserModel(jsonResponse);
                }

                @Override
                public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                    Log.d("Error",t.getMessage());
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.help) {
            Toast.makeText(PlayActivity.this, results.toString(), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            finish();
            return true;
        }
    }
}
