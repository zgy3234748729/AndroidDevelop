package com.zeek.studentmgr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.Toast;

public class ActivityAds extends AppCompatActivity {
    private static final int AD_DURATION = 5000;    //5秒
    private Button skipButton;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        Toast.makeText(this, "刚开始广告页", Toast.LENGTH_SHORT).show();

        //找到跳过按钮
        skipButton = findViewById(R.id.skip_button);

        //创建倒计时计时器
        countDownTimer = new CountDownTimer(AD_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) millisUntilFinished / 1000;
                skipButton.setText(getString(R.string.skip_button_text, secondsRemaining));
            }

            @Override
            public void onFinish() {
                //计时结束，执行跳转到主界面的操作
                startMainActivity();
            }
        };
        countDownTimer.start();


        skipButton.setOnClickListener(v -> {
            startMainActivity();
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在Activity销毁时，停止倒计时计时器以避免内存泄露
        countDownTimer.cancel();
    }
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}