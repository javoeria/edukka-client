package com.javier.edukka.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.javier.edukka.R;
import com.javier.edukka.model.UserModel;
import com.javier.edukka.service.RestInterface;
import com.javier.edukka.service.RetrofitClient;
import com.javier.edukka.view.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListContentFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ContentAdapter mAdapter;
    public static ArrayList<UserModel> mArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        loadJSON();
        return mRecyclerView;
    }

    private void loadJSON(){
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<List<UserModel>> call = restInterface.getAllUsers();
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                List<UserModel> jsonResponse = response.body();
                mArrayList = (ArrayList<UserModel>) jsonResponse;
                mAdapter = new ContentAdapter(mArrayList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<UserModel> mArrayList;

        public ContentAdapter(ArrayList<UserModel> arrayList) {
            mArrayList = arrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            holder.avatar.setImageResource(R.drawable.dog);
            holder.name.setText(mArrayList.get(i).getUsername());
            holder.description.setText(mArrayList.get(i).getName());
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public TextView name;
        public TextView description;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            avatar = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ProfileActivity.class);
                    Integer i = Integer.parseInt(mArrayList.get(getAdapterPosition()).getId());
                    intent.putExtra(ProfileActivity.EXTRA_POSITION, i);
                    context.startActivity(intent);
                }
            });
        }
    }

}
