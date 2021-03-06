package com.javier.edukka.adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Vibrator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javier.edukka.R;
import com.javier.edukka.service.DoubleClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DragDropAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<String> questions;
    private final List<String> options;
    private final List<String> answer;
    private int cont = 0;

    public DragDropAdapter(Context applicationContext, List<String> questions, List<String> options) {
        this.inflater = (LayoutInflater.from(applicationContext));
        this.questions = questions;
        this.options = options;
        this.answer = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int i) {
        StringBuilder res = new StringBuilder();
        for (String s : answer) {
            res.append(s);
        }
        if (res.length()>0) {
            res.deleteCharAt(0);
        }
        return res.toString();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (i != 0 || cont != questions.size()-1) {
            view = inflater.inflate(R.layout.play_dragdrop, viewGroup, false);
            answer.clear();
            TextView question = (TextView) view.findViewById(R.id.question);
            question.setText(questions.get(i));
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvOptions);
            recyclerView.setLayoutManager(new GridLayoutManager(viewGroup.getContext(), 4));
            MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(view.getContext(), view.findViewById(R.id.answerLayout), Arrays.asList(options.get(i).split(",")));
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);

            View.OnDragListener dragListener = new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    switch (dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_ENTERED:
                            view.getBackground().setColorFilter(0xff33b5e5, PorterDuff.Mode.SRC_IN);
                            view.invalidate();
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            break;
                        case DragEvent.ACTION_DROP:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            View v = (View) dragEvent.getLocalState();
                            ViewGroup owner = (ViewGroup) v.getParent();
                            owner.removeView(v);
                            LinearLayout container = (LinearLayout) view;
                            if (owner.getId() == R.id.answerLayout && container.getId() == R.id.answerLayout) {
                                answer.remove(dragEvent.getClipData().getItemAt(0).getText().toString());
                                answer.add(dragEvent.getClipData().getItemAt(0).getText().toString());
                                container.addView(v);
                            } else if (container.getId() == R.id.answerLayout) {
                                answer.add(dragEvent.getClipData().getItemAt(0).getText().toString());
                                container.addView(v);
                            } else {
                                answer.remove(dragEvent.getClipData().getItemAt(0).getText().toString());
                            }
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            };
            view.findViewById(R.id.optionsLayout).setOnDragListener(dragListener);
            view.findViewById(R.id.answerLayout).setOnDragListener(dragListener);
        }
        cont = i;
        return view;
    }

    class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private final LayoutInflater mInflater;
        private final View layoutView;
        private final List<String> mData;

        MyRecyclerViewAdapter(Context context, View layoutView, List<String> data) {
            this.mInflater = LayoutInflater.from(context);
            this.layoutView = layoutView;
            this.mData = data;
        }

        @Override
        public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.recyclerview_text, parent, false);
            return new MyRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyRecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.textView.setText(" "+mData.get(position));
            holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Vibrator v = (Vibrator) inflater.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(100);
                    ClipData data = ClipData.newPlainText("", holder.textView.getText().toString());
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    return true;
                }
            });
            holder.textView.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onDoubleClick(View view) {
                    LinearLayout container = (LinearLayout) layoutView;
                    container.removeView(view);
                    container.invalidate();
                    ClipData data = ClipData.newPlainText("", holder.textView.getText().toString());
                    answer.remove(data.getItemAt(0).getText().toString());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }
}
