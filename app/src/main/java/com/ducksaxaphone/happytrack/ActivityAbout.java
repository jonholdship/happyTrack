package com.ducksaxaphone.happytrack;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAbout extends BaseActivity {
    @BindView(R.id.AboutText) TextView aboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);
        String html = getString(R.string.about_text);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            aboutText.setText(Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY));
        } else {
            aboutText.setText( Html.fromHtml(html));
        }
        super.onCreate(savedInstanceState);


    }
}
