package com.javier.edukka.view;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
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

        subject = getIntent().getStringExtra(SUBJECT_NAME);
        getSupportActionBar().setTitle(subject);

        initViews();
        loadJSON();
    }

    private void initViews(){
        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadJSON(){
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<List<GameModel>> call = restInterface.getSubjectGames(subject);
        call.enqueue(new Callback<List<GameModel>>() {
            @Override
            public void onResponse(Call<List<GameModel>> call, Response<List<GameModel>> response) {
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
            public void onFailure(Call<List<GameModel>> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
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
}
