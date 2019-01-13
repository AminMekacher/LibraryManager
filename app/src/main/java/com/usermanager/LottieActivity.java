package com.usermanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.usermanager_demo.R;

public class LottieActivity extends AppCompatActivity {

    private LottieAnimationView mLottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);

        mLottie = findViewById(R.id.book_animation);
        mLottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LottieActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLottie.playAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLottie.cancelAnimation();
    }
}
