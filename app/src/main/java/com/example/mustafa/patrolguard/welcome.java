package com.example.mustafa.patrolguard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
    public void startpatrol(View view)
    {
        Intent a = new Intent(this,Main2Activity.class);
        startActivity(a);
    }
    public void assigntsk(View view)
    {
        Intent a = new Intent(this,MainActivity.class);
        startActivity(a);
    }
}
