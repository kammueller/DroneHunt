package de.hackatum.mediasaturn.userapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import de.hackatum.mediasaturn.userapp.R;

/**
 * A small activity which does nothing but add a little nice intro to the application and call the MainActivity afterwards.
 *
 * Created by Alexander Lannes on 12.11.2016.
 */

public class IntroActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(de.hackatum.mediasaturn.userapp.R.layout.activity_intro);
        ImageView img= (ImageView) findViewById(R.id.imageView);
        img.setImageResource(R.drawable.logo_1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(de.hackatum.mediasaturn.userapp.R.layout.activity_intro2);
                ImageView img= (ImageView) findViewById(R.id.imageView);
                img.setImageResource(R.drawable.mediasaturnlogo);
            }
        }, 3000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(de.hackatum.mediasaturn.userapp.R.layout.activity_intro3);
            }
        }, 5000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 7000);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
