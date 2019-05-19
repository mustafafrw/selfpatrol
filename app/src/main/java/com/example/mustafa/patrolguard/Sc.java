package com.example.mustafa.patrolguard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Sc extends AppCompatActivity {

    TextView scx;
    public static String msg="TASK ASSIGNED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sc);

        scx = findViewById(R.id.textView4);
        scx.setText(msg);

    }
}
