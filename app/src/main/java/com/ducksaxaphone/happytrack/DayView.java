package com.ducksaxaphone.happytrack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

/**
 * TODO: document your custom view class.
 */
public class DayView extends RelativeLayout {

    @BindView (R.id.HappySlider) DiscreteSeekBar happySlider;
    @BindView (R.id.HappyFace) ImageView happyFace;
    @BindView (R.id.dateText) TextView dateText;
    @BindView (R.id.NotesText) EditText notesBox;
    public Day currentDay;

    public DayView(Context context) {
        super(context);
    }

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_day, this, true);
        ButterKnife.bind(this,view);

        happySlider.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                int progress = seekBar.getProgress();
                currentDay.setRating(progress);
                changeFace(progress);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

    }

    private void changeFace(int sliderPos){
        if (sliderPos<3){
            happyFace.setImageResource(R.drawable.sadface2);

        }
        else if (sliderPos<5){
            happyFace.setImageResource(R.drawable.sadface1);
        }
        else if (sliderPos < 7){
            happyFace.setImageResource(R.drawable.mediumface);
        }
        else if (sliderPos <9){
            happyFace.setImageResource(R.drawable.happyface1);
        }
        else{
            happyFace.setImageResource(R.drawable.happyface2);
        }



    }

    @OnTextChanged(R.id.NotesText)
    public void onNotesChanged(){
        currentDay.setNotes(notesBox.getText().toString());
    }



    public void setDay(Day day){
        currentDay =day;
        happySlider.setProgress(day.getRating());
        changeFace(day.getRating());

        dateText.setText(day.getDate());
        notesBox.setText(day.getNotes());

    }
}

