package com.javier.edukka.views;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.javier.edukka.R;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        // collapsingToolbar.setTitle(getString(R.string.item_title));

        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        Resources resources = getResources();
        String[] subjects = resources.getStringArray(R.array.subjects);
        collapsingToolbar.setTitle(subjects[position % subjects.length]);

        String[] subjectDetails = resources.getStringArray(R.array.subject_details);
        TextView subjectDetail = (TextView) findViewById(R.id.subject_detail);
        subjectDetail.setText(subjectDetails[position % subjectDetails.length]);

        String[] subjectLocations = resources.getStringArray(R.array.subject_locations);
        TextView subjectLocation =  (TextView) findViewById(R.id.subject_location);
        subjectLocation.setText(subjectLocations[position % subjectLocations.length]);

        TypedArray subjectPictures = resources.obtainTypedArray(R.array.subject_pictures);
        ImageView subjectPicutre = (ImageView) findViewById(R.id.image);
        subjectPicutre.setImageDrawable(subjectPictures.getDrawable(position % subjectPictures.length()));

        subjectPictures.recycle();
    }
}
