package com.zero.progressview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressView progressView = (ProgressView) findViewById(R.id.progress);
        progressView.setCurrentCount(1000,100);
        progressView.setCommandClickListener(new ProgressView.CommandClickListener() {
            @Override
            public void onCommandClick(View view) {
                Toast.makeText(getBaseContext(),view.getId()+"",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
