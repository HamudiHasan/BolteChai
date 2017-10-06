package com.aims.boltechai.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.aims.boltechai.R;
/**
 * About Application page
 * Contains some static contains
 */
public class AboutActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        textView = (TextView) findViewById(R.id.tv_about);

        Spanned text = Html.fromHtml("Developed By <b>AIMS LAB</b> in Partnarship with <b> TAURI Foundation (School For Gifted Children)</b>. Supported By <b>ICT Division</b> ");
        textView.setText(text);
    }
}
