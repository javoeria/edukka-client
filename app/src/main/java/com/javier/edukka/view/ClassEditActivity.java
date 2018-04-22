package com.javier.edukka.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.javier.edukka.R;
import com.javier.edukka.model.ClassModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassEditActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    private TextView name, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_edit);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.editclass);

        name = findViewById(R.id.class_name);
        info = findViewById(R.id.class_info);
        loadJSON();
    }

    private void loadJSON(){
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<ClassModel> call = restInterface.getClass(position);
        call.enqueue(new Callback<ClassModel>() {
            @Override
            public void onResponse(@NonNull Call<ClassModel> call, @NonNull Response<ClassModel> response) {
                ClassModel jsonResponse = response.body();
                name.setText(jsonResponse.getName());
                info.setText(jsonResponse.getInformation());
            }

            @Override
            public void onFailure(@NonNull Call<ClassModel> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    private boolean checkFieldValidation() {
        boolean valid = true;
        if (name.getText().toString().equals("")) {
            name.setError("Can't be Empty");
            valid = false;
        } else if (info.getText().toString().equals("")) {
            info.setError("Can't be Empty");
            valid = false;
        }
        return valid;
    }

    public void save(View v) {
        if (checkFieldValidation()) {
            int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            RestInterface restInterface = RetrofitClient.getInstance();
            Call<ClassModel> call = restInterface.updateClass(name.getText().toString(), info.getText().toString(), position);
            call.enqueue(new Callback<ClassModel>() {
                @Override
                public void onResponse(@NonNull Call<ClassModel> call, @NonNull Response<ClassModel> response) {
                    Toast.makeText(ClassEditActivity.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<ClassModel> call, @NonNull Throwable t) {
                    Log.d("Error",t.getMessage());
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
