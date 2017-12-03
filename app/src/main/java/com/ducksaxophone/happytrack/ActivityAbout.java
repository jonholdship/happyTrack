package com.ducksaxophone.happytrack;

import android.os.Bundle;
import android.text.Html;
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
