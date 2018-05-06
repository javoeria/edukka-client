package com.javier.edukka.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.javier.edukka.R;

import java.util.List;

public class PickerAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<String> questions;
    private final List<String> answers;
    private TextView answer;
    private int cont = 0;

    public PickerAdapter(Context applicationContext, List<String> questions, List<String> answers) {
        this.inflater = (LayoutInflater.from(applicationContext));
        this.questions = questions;
        this.answers = answers;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public int getAnswer() {
        return Integer.parseInt(answer.getText().toString());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (cont < questions.size()) {
            view = inflater.inflate(R.layout.play_quiz, viewGroup, false);
            final int min = (Integer.parseInt(answers.get(i))/10)*10;
            TextView question = (TextView) view.findViewById(R.id.question);
            question.setText(questions.get(i));
            answer = (TextView) view.findViewById(R.id.answer);
            answer.setText(String.valueOf(min));
            SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
            seekBar.setMax(10);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    answer.setText(String.valueOf(i+min));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }
        cont++;
        return view;
    }
}
