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
import android.widget.TextView;

import com.javier.edukka.R;
import com.javier.edukka.controller.UserSingleton;
import com.javier.edukka.fragment.ListContentFragment;
import com.javier.edukka.model.ClassModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassActivity extends AppCompatActivity {

    private static final String EXTRA_POSITION = "position";
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton fab;
    private TextView id, info, size;
    private boolean create = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        id = (TextView) findViewById(R.id.class_id);
        info = (TextView) findViewById(R.id.class_info);
        size = (TextView) findViewById(R.id.class_size);
        loadJSON();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create) {
                    Intent intent = new Intent(ClassActivity.this, ClassNewActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ClassActivity.this, ClassEditActivity.class);
                    int id = getIntent().getIntExtra(EXTRA_POSITION, 0);
                    intent.putExtra(ClassEditActivity.EXTRA_POSITION, id);
                    startActivity(intent);
                }
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
        int position = Integer.parseInt(UserSingleton.getInstance().getUserModel().getClassId());
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<ClassModel> call = restInterface.getClass(position);
        call.enqueue(new Callback<ClassModel>() {
            @Override
            public void onResponse(@NonNull Call<ClassModel> call, @NonNull Response<ClassModel> response) {
                ClassModel jsonResponse = response.body();
                collapsingToolbar.setTitle(jsonResponse.getName());
                id.setText(jsonResponse.getId());
                info.setText(jsonResponse.getInformation());
                size.setText(getString(R.string.class_size,ListContentFragment.getSize()));
                if (UserSingleton.getInstance().getUserModel().getId().equals(jsonResponse.getTeacherId())) {
                    fab.setVisibility(View.VISIBLE);
                } else if (UserSingleton.getInstance().getUserModel().getRole().equals("teacher") &&
                           UserSingleton.getInstance().getUserModel().getClassId().equals("0")) {
                    fab.setImageResource(android.R.drawable.ic_input_add);
                    fab.setVisibility(View.VISIBLE);
                    create = true;
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClassModel> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }
}
