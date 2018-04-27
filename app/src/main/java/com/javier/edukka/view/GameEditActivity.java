package com.javier.edukka.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.javier.edukka.R;
import com.javier.edukka.model.GameModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameEditActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    private TextView name, desc;
    private MaterialBetterSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_edit);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.editgame);

        name = (TextView) findViewById(R.id.game_name);
        desc = (TextView) findViewById(R.id.game_desc);
        spinner = (MaterialBetterSpinner) findViewById(R.id.game_level);

        String[] level_list = getResources().getStringArray(R.array.level);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, level_list);
        spinner.setAdapter(arrayAdapter);
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
                name.setText(jsonResponse.getTitle());
                desc.setText(jsonResponse.getDescription());
                spinner.setText(jsonResponse.getDifficulty());
            }

            @Override
            public void onFailure(@NonNull Call<GameModel> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    private boolean checkFieldValidation() {
        boolean valid = true;
        if (name.getText().toString().equals("")) {
            name.setError(getText(R.string.empty));
            valid = false;
        } else if (desc.getText().toString().equals("")) {
            desc.setError(getText(R.string.empty));
            valid = false;
        }
        return valid;
    }

    public void save(View v) {
        if (checkFieldValidation()) {
            int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            RestInterface restInterface = RetrofitClient.getInstance();
            Call<GameModel> call = restInterface.updateGame(name.getText().toString(), desc.getText().toString(), spinner.getText().toString(), position);
            call.enqueue(new Callback<GameModel>() {
                @Override
                public void onResponse(@NonNull Call<GameModel> call, @NonNull Response<GameModel> response) {
                    Toast.makeText(GameEditActivity.this, R.string.data_update, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<GameModel> call, @NonNull Throwable t) {
                    Log.d("Error",t.getMessage());
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            infoDialog();
            return true;
        } else {
            finish();
            return true;
        }
    }

    private void infoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.deletegame);
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                setContentView(R.layout.progressbar_layout);
                deleteGame();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        View dialogView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
        TextView textView1 = (TextView) dialogView.findViewById(android.R.id.text1);
        textView1.setText(R.string.dialoggame);
        builder.setView(dialogView);
        builder.show();
    }

    private void deleteGame(){
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<Void> call = restInterface.deleteGame(position);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Intent i = new Intent(GameEditActivity.this, MainActivity.class);
                finish();
                startActivity(i);
                Toast.makeText(GameEditActivity.this, R.string.deletegame_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

}
