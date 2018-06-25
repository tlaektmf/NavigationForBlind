package com.example.dazze.navigationforblind;

import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class NIntroActivity extends AppCompatActivity {

    ImageView imageView;

    FrameLayout frameLayout;
    ImageView userChoiceText;

    private Handler mHandler;
    private Runnable mRunnable;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nintro);

        hideActionBar();

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREA);
            }
        });

        imageView = (ImageView) findViewById(R.id.introImageView);
        userChoiceText = (ImageView) findViewById(R.id.userChoiceText);
        frameLayout = (FrameLayout) findViewById(R.id.introFrameLayout);

        int img_intro = R.drawable.intro;
        Glide.with(this).load(img_intro).into(imageView);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                task();
            }
        };

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 3000);

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    tts.speak("시각장애인 모드 입니다."
                            ,TextToSpeech.QUEUE_ADD, null, null);

                    Intent intent = new Intent(getApplicationContext(), NUIntroMenuActivity.class);
                    startActivity(intent);
                    finish();

                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    tts.speak("보호자 모드 입니다."
                            ,TextToSpeech.QUEUE_ADD, null, null);
                    return super.onSingleTapConfirmed(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.hide();
        }
    }

    public void task(){
        int img_user_choice;
        img_user_choice = R.drawable.user_choice;
        Glide.with(this).load(img_user_choice).into(imageView);

        userChoiceText.setVisibility(View.VISIBLE);

        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        userChoiceText.startAnimation(animation);

        tts.speak("안녕하세요. EYE NETWORK 입니다. 시각장애인시면 두 번, 보호자이시면 한 번 탭해주세요."
                ,TextToSpeech.QUEUE_ADD, null, null);
    }
}
