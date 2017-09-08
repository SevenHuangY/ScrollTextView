package com.qinggan.testtextview;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity
{
    private AutoScrollTextView mAutoScrollTextView;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAutoScrollTextView = (AutoScrollTextView) findViewById(R.id.autoScrollTextView);
        mAutoScrollTextView.setScrollInterval(500);
        content = getResources().getString(R.string.content);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAutoScrollTextView.setText(content);
            }
        });

        Button stopBtn = (Button) findViewById(R.id.stop_btn);
        stopBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAutoScrollTextView.cancel();
            }
        });
    }


}
