package br.rgb.openweatherreaderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewPropertyAnimator;

public class LoadingActivity extends Activity {
    public static final int REQUEST_LOADING_SCREEN = 1104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        ViewPropertyAnimator animate = findViewById(R.id.bg).animate();
        animate.alpha(0.44f);
        animate.setDuration(350);
        animate.start();
    }

    public static void openFrom(Activity src){
        src.startActivityForResult(new Intent(src, LoadingActivity.class), REQUEST_LOADING_SCREEN);
    }

    public static void closeFrom(Activity of){
        of.finishActivity(REQUEST_LOADING_SCREEN);
    }
}
