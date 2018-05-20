package com.javier.edukka.adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javier.edukka.R;

import java.util.Arrays;
import java.util.List;

public class DragNameAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<String> questions;
    private final List<String> options;
    private final String[] answer;
    private int cont = 0;

    public DragNameAdapter(Context applicationContext, List<String> questions, List<String> options) {
        this.inflater = (LayoutInflater.from(applicationContext));
        this.questions = questions;
        this.options = options;
        this.answer = new String[3];
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int i) {
        StringBuilder res = new StringBuilder();
        res.append(Arrays.asList(answer).toString());
        res.deleteCharAt(res.length()-1);
        res.deleteCharAt(0);
        return res.toString();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (i != 0 || cont != questions.size() - 1) {
            view = inflater.inflate(R.layout.play_dragname, viewGroup, false);
            Arrays.fill(answer," ");
            TextView question = (TextView) view.findViewById(R.id.question);
            final TextView answer1 = (TextView) view.findViewById(R.id.textView1);
            final TextView answer2 = (TextView) view.findViewById(R.id.textView2);
            final TextView answer3 = (TextView) view.findViewById(R.id.textView3);
            question.setText(questions.get(i));
            answer1.setText(options.get(i).split(",")[0]);
            answer2.setText(options.get(i).split(",")[1]);
            answer3.setText(options.get(i).split(",")[2]);

            answer1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData data = ClipData.newPlainText("", answer1.getText().toString());
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    answer1.setVisibility(View.INVISIBLE);
                    return true;
                }
            });
            answer2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData data = ClipData.newPlainText("", answer2.getText().toString());
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    answer2.setVisibility(View.INVISIBLE);
                    return true;
                }
            });
            answer3.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData data = ClipData.newPlainText("", answer3.getText().toString());
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    answer3.setVisibility(View.INVISIBLE);
                    return true;
                }
            });

            View.OnDragListener dragListener = new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    switch (dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            break;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            view.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
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
                            container.addView(v);
                            v.setVisibility(View.VISIBLE);
                            switch (owner.getId()) {
                                case R.id.answerLayout1:
                                    answer[0] = " ";
                                    break;
                                case R.id.answerLayout2:
                                    answer[1] = " ";
                                    break;
                                case R.id.answerLayout3:
                                    answer[2] = " ";
                                    break;
                            }
                            switch (container.getId()) {
                                case R.id.answerLayout1:
                                    answer[0] = dragEvent.getClipData().getItemAt(0).getText().toString();
                                    break;
                                case R.id.answerLayout2:
                                    answer[1] = dragEvent.getClipData().getItemAt(0).getText().toString();
                                    break;
                                case R.id.answerLayout3:
                                    answer[2] = dragEvent.getClipData().getItemAt(0).getText().toString();
                                    break;
                            }
                            break;
                        default:
                            View v1 = (View) dragEvent.getLocalState();
                            v1.setVisibility(View.VISIBLE);
                            break;
                    }
                    return true;
                }
            };
            view.findViewById(R.id.optionsLayout).setOnDragListener(dragListener);
            view.findViewById(R.id.answerLayout1).setOnDragListener(dragListener);
            view.findViewById(R.id.answerLayout2).setOnDragListener(dragListener);
            view.findViewById(R.id.answerLayout3).setOnDragListener(dragListener);
        }
        cont = i;
        return view;
    }
}
