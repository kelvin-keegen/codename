package com.mobi_apps.kelvinkeegan.codenamealarmclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ImageView pic = (ImageView) findViewById(R.id.imageView);
        Animation clockpic = AnimationUtils.loadAnimation(this, R.anim.imageanime);
        pic.startAnimation(clockpic);

        TextView textanime = (TextView) findViewById(R.id.textView);
        Animation minimalist = AnimationUtils.loadAnimation(this, R.anim.textanimations);
        textanime.startAnimation(minimalist);

        minimalist.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {


            }

            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this,AlarmActivity.class));
                SplashActivity.this.finish();
            }

            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        ImageView pic = (ImageView) findViewById(R.id.imageView);
        pic.clearAnimation();

        TextView textanime = (TextView) findViewById(R.id.textView);
        textanime.clearAnimation();
    }


}
