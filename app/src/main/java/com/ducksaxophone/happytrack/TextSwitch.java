package com.ducksaxophone.happytrack;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Simple view, a two button "switch"
 * User clicks never or daily and that button is highlighted in colorPrimary.
 * Contained in colorPrimary rounded corner border.
 */
public class TextSwitch extends LinearLayout {

    interface TextSwitchListener {
        void leftButtonPressed();
        void rightButtonPressed();

    }
    @BindView(R.id.ButtonDaily)
    Button dailyButton;
    @BindView(R.id.ButtonNever) Button neverButton;

    private TextSwitchListener mListener;

    public TextSwitch(Context context) {
        super(context);
    }

    public TextSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_text_switch, this, true);
        ButterKnife.bind(this,view);
    }

    public void setListener(TextSwitchListener listen){
        mListener=listen;
    }

    @OnClick(R.id.ButtonDaily)
    public void onDailyClick(){
        Resources res=getResources();

        //change buttons to look like daily is selected
        dailyButton.setBackground(getResources().getDrawable(R.drawable.rightboxborder));
        dailyButton.setTextColor(res.getColor(R.color.colorIcons));

        neverButton.setBackgroundColor(0x00000000);
        neverButton.setTextColor(res.getColor(R.color.colorPrimary));

        mListener.rightButtonPressed();
    }

    @OnClick(R.id.ButtonNever)
    public void onNeverClick(){
        Resources res=getResources();
        neverButton.setBackground(res.getDrawable(R.drawable.leftboxborder));
        neverButton.setTextColor(res.getColor(R.color.colorIcons));

        dailyButton.setBackgroundColor(0x00000000);
        dailyButton.setTextColor(res.getColor(R.color.colorPrimary));

        mListener.leftButtonPressed();
    }


}
