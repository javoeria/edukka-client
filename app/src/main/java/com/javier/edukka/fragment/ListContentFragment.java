package com.javier.edukka.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
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
    private static ArrayList<UserModel> mArrayList;
    private static Integer size;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        loadJSON();
        return mRecyclerView;
    }

    public static Integer getSize() {
        return size;
    }

    private void loadJSON(){
        RestInterface restInterface = RetrofitClient.getInstance();
        Call<List<UserModel>> call = restInterface.getAllUsers();
        //Integer id = Integer.parseInt(UserSingleton.getInstance().getUserModel().getClassId());
        //Call<List<UserModel>> call = restInterface.getUserClass(id);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserModel>> call, @NonNull Response<List<UserModel>> response) {
                List<UserModel> jsonResponse = response.body();
                mArrayList = (ArrayList<UserModel>) jsonResponse;
                mAdapter = new ContentAdapter(mArrayList);
                mRecyclerView.setAdapter(mAdapter);
                size = mArrayList.size();
            }

            @Override
            public void onFailure(@NonNull Call<List<UserModel>> call, @NonNull Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final ArrayList<UserModel> mArrayList;

        private ContentAdapter(ArrayList<UserModel> arrayList) {
            mArrayList = arrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            int resourceId = getResources().getIdentifier(mArrayList.get(i).getImage(), "drawable", getContext().getPackageName());
            holder.avatar.setImageResource(resourceId);
            holder.name.setText(mArrayList.get(i).getUsername());
            holder.description.setText(mArrayList.get(i).getName());
            if (mArrayList.get(i).getRole().equals("Student")) {
                holder.score.setText(mArrayList.get(i).getScore());
            }
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView avatar;
        private final TextView name;
        private final TextView description;
        private final TextView score;

        private ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            avatar = itemView.findViewById(R.id.list_avatar);
            name = itemView.findViewById(R.id.list_title);
            description = itemView.findViewById(R.id.list_desc);
            score = itemView.findViewById(R.id.list_score);

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
