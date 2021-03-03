package com.example.androidsubject.MVVM;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.androidsubject.R;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class IntroActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragment(new Step.Builder().setTitle("JET GPS SHARE")
                .setContent("WELCOME !!!")
                .setBackgroundColor(Color.parseColor("#a1c4fd")) // int background color
                .setDrawable(R.drawable.ic_access_alarm_black_24dp) // int top drawable
                .build());
        // Permission Step

        addFragment(new Step.Builder().setTitle("JET GPS SHARE")
                .setContent("THIS APP NEEDS CONTACTS,LOCATION PERMISSION.")
                .setBackgroundColor(Color.parseColor("#a1c4fd")) // int background color
                .setDrawable(R.drawable.ic_access_alarm_black_24dp) // int top drawable
                .setSummary("Please give permission for working of the app.")
                .build());
    }

    @Override
    public void currentFragmentPosition(int position) {

    }
    @Override
    public void finishTutorial() {
        Intent i = new Intent(getApplicationContext(), MyActivity.class);
        startActivity(i);
        finish();
    }
}
