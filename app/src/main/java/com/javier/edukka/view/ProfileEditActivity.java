package com.javier.edukka.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.javier.edukka.R;
import com.javier.edukka.adapter.AvatarAdapter;
import com.javier.edukka.controller.UserSingleton;
import com.javier.edukka.model.UserModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    private TextView name, pass, classid;
    private List<Drawable> avatars;
    private AvatarAdapter avatarAdapter;
    private final String[] array = {"avatar_butterfly", "avatar_cat", "avatar_dog", "avatar_elephant",
            "avatar_lion", "avatar_panda", "avatar_snake", "avatar_spider", "avatar_turtle", "avatar_wolf", "avatar_teacher"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.editprofile);

        name = findViewById(R.id.user_name);
        pass = findViewById(R.id.user_pass);
        classid = findViewById(R.id.user_classid);

        initAvatars();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        avatarAdapter = new AvatarAdapter(avatars);
        recyclerView.setAdapter(avatarAdapter);
        loadJSON();
    }

    private void initAvatars() {
        avatars = new ArrayList<>();
        TypedArray a = getResources().obtainTypedArray(R.array.avatar_pictures);
        for (int i=0; i<array.length; i++) {
            avatars.add(a.getDrawable(i));
        }
        a.recycle();
    }

    private void loadJSON(){
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<UserModel> call = restInterface.getUser(position);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                UserModel jsonResponse = response.body();
                name.setText(jsonResponse.getName());
                classid.setText(jsonResponse.getClassId());
                int pos = Arrays.asList(array).indexOf(jsonResponse.getImage());
                avatarAdapter.setSelectedPos(pos);
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    private boolean checkFieldValidation() {
        boolean valid = true;
        if (name.getText().toString().equals("")) {
            name.setError("Can't be Empty");
            valid = false;
        }
        return valid;
    }

    public void save(View v) {
        if (checkFieldValidation()) {
            int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            RestInterface restInterface = RetrofitClient.getInstance();
            Call<UserModel> call = restInterface.updateUser(name.getText().toString(), pass.getText().toString(),
                    array[avatarAdapter.getSelectedPos()], Integer.parseInt(classid.getText().toString()), position);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                    UserModel jsonResponse = response.body();
                    if (jsonResponse.getId()==null) {
                        Toast.makeText(ProfileEditActivity.this, "Clase no existe", Toast.LENGTH_SHORT).show();
                    } else {
                        UserSingleton.getInstance().setUserModel(jsonResponse);
                        Toast.makeText(ProfileEditActivity.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                        finish();
                    }
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
        builder.setTitle("Delete User");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                setContentView(R.layout.progressbar_layout);
                deleteUser();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        View dialogView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
        TextView textView1 = dialogView.findViewById(android.R.id.text1);
        textView1.setText("Esta acción borrará tu usuario de la base de datos, ¿estás seguro?");
        builder.setView(dialogView);
        builder.show();
    }

    private void deleteUser(){
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        String role = UserSingleton.getInstance().getUserModel().getRole();
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<Void> call = restInterface.deleteUser(position, role);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Intent i = new Intent(ProfileEditActivity.this, LoginActivity.class);
                finish();
                startActivity(i);
                Toast.makeText(ProfileEditActivity.this, "User Deleted Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

}
