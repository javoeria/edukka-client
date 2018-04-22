package com.javier.edukka.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.javier.edukka.R;
import com.javier.edukka.adapter.GameAdapter;
import com.javier.edukka.controller.UserSingleton;
import com.javier.edukka.model.GameModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    public static final String SUBJECT_NAME = "subject";
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<GameModel> mArrayList;
    private GameAdapter mAdapter;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        subject = getIntent().getStringExtra(SUBJECT_NAME);
        getSupportActionBar().setTitle(subject);
        initViews();
        loadJSON();
        refresh();

        FloatingActionButton fab = findViewById(R.id.fab);
        if (UserSingleton.getInstance().getUserModel().getRole().equals("Teacher")) {
            fab.setVisibility(View.VISIBLE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    private void initViews(){
        mRecyclerView = findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadJSON(){
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<List<GameModel>> call = restInterface.getSubjectGames(subject);
        call.enqueue(new Callback<List<GameModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<GameModel>> call, @NonNull Response<List<GameModel>> response) {
                List<GameModel> jsonResponse = response.body();
                if (jsonResponse.get(0).getId()==null) {
                    mArrayList = new ArrayList<>();
                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                } else {
                    mArrayList = (ArrayList<GameModel>) jsonResponse;
                }
                mAdapter = new GameAdapter(mArrayList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<GameModel>> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            return super.onOptionsItemSelected(item);
        } else {
            finish();
            return true;
        }
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void refresh() {
        mySwipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        );
    }
}
