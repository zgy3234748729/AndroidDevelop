package com.zeek.studentmgr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ActivityLogin extends AppCompatActivity {
    private Button loginButton;
    private EditText userNameEdit;
    private EditText passwordEdit;
    private ProgressBar horizonP;
    private int mProgressStatus = 0;
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //获取引用
        loginButton = findViewById(R.id.loginBtn);
        userNameEdit = findViewById(R.id.userName);
        passwordEdit = findViewById(R.id.password);
        horizonP = findViewById(R.id.progressBar);


        loginButton.setOnClickListener(view -> {


            //进度条
            //设置进度条可见
//            horizonP.setVisibility(View.VISIBLE);
            // 创建一个后台线程来模拟进度更新
//            new Thread(new Runnable() {
//                public void run() {
//                    while (mProgressStatus < 100) {
//                        mProgressStatus += 1;
//                        // 使用Handler更新进度条，将进度条更新操作放在主线程中
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                horizonP.setProgress(mProgressStatus);
//                            }
//                        });
//
//                        try {
//                            // 休眠三十毫秒，模拟进度更新
//                            Thread.sleep(10);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    //进度条完成后执行验证
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            validateLogin();
//                        }
//                    });
//
//                }
//            }).start();



            //TODO:显示一个广告页面
            validateLoginAds();
        });
    }
    private void validateLogin() {
        String username = userNameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String validName = getResources().getString(R.string.username);
        String validPassword = getResources().getString(R.string.password);

        //验证是否通过
        if (username.equals(validName) && password.equals(validPassword)) {
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            // 登录失败，显示消息并恢复原样
            Toast.makeText(this, "登录失败，请重新输入", Toast.LENGTH_SHORT).show();
            horizonP.setVisibility(View.INVISIBLE); // 隐藏进度条
            mProgressStatus = 0; // 重置进度
        }
    }

    private void validateLoginAds() {
        String username = userNameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String validName = getResources().getString(R.string.username);
        String validPassword = getResources().getString(R.string.password);

        //验证是否通过
        if (username.equals(validName) && password.equals(validPassword)) {
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            //开启广告页
            Intent intent = new Intent(this, ActivityAds.class);
            startActivity(intent);
        } else {
            // 登录失败，显示消息并恢复原样
            Toast.makeText(this, "登录失败，请重新输入", Toast.LENGTH_SHORT).show();
        }
    }
}