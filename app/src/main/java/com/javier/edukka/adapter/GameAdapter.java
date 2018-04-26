package com.javier.edukka.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.javier.edukka.R;
import com.javier.edukka.model.GameModel;
import com.javier.edukka.view.GameActivity;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> implements Filterable {
    private final ArrayList<GameModel> mArrayList;
    private ArrayList<GameModel> mFilteredList;

    public GameAdapter(ArrayList<GameModel> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.game_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.game_name.setText(mFilteredList.get(i).getTitle());
        viewHolder.game_desc.setText(mFilteredList.get(i).getDescription());
        viewHolder.game_level.setText(mFilteredList.get(i).getDifficulty());
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<GameModel> filteredList = new ArrayList<>();
                    for (GameModel gameModel : mArrayList) {
                        if (gameModel.getTitle().toLowerCase().contains(charString)) {
                            filteredList.add(gameModel);
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<GameModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView game_name;
        private final TextView game_desc;
        private final TextView game_level;

        private ViewHolder(View view) {
            super(view);
            game_name = view.findViewById(R.id.game_name);
            game_desc = view.findViewById(R.id.game_desc);
            game_level = view.findViewById(R.id.game_level);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, GameActivity.class);
                    Integer i = Integer.parseInt(mFilteredList.get(getAdapterPosition()).getId());
                    String s = mFilteredList.get(getAdapterPosition()).getSubject();
                    intent.putExtra(GameActivity.EXTRA_POSITION, i);
                    intent.putExtra(GameActivity.EXTRA_SUBJECT, s);
                    context.startActivity(intent);
                }
            });
        }
    }

}