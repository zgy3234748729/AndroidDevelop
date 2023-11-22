package com.zeek.studentmgr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Objects;

public class ActivityLogin extends AppCompatActivity {
    private Button loginButton;
    private EditText userNameEdit;
    private EditText passwordEdit;
    private ProgressBar horizonP;
    private int mProgressStatus = 0;
    private Handler handler = new Handler(Looper.getMainLooper());

    private CheckBox savePassword;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 获得SharedPreferences，并指定文件名称为”config“
        final SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);

        // 获得Editor对象，用于存储用户名与密码信心
        final SharedPreferences.Editor editor = sp.edit();

        //获取引用
        loginButton = findViewById(R.id.loginBtn);
        userNameEdit = findViewById(R.id.userName);
        passwordEdit = findViewById(R.id.password);
        horizonP = findViewById(R.id.progressBar);
        savePassword = findViewById(R.id.save_password_checkBox);

        if (sp.getString("username", null) != null && sp.getString("password", null) != null) {
            userNameEdit.setText(sp.getString("username", null));
            passwordEdit.setText(sp.getString("password", null));
            savePassword.setChecked(true);
        }

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
            String username = userNameEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            String validName = getResources().getString(R.string.username);
            String validPassword = getResources().getString(R.string.password);



            //验证是否通过
            if (username.equals(validName) && password.equals(validPassword)) {
                // 检查保存密码复选框是否选中，选中则保存密码到SharedPreference
                savePassword = findViewById(R.id.save_password_checkBox);
                if (savePassword.isChecked()) {
                    editor.putString("username", username);     // 存储账号
                    editor.putString("password", password);     // 存储密码
                    editor.apply();                            // 提交信息
                    Toast.makeText(this, "已保存账号和密码", Toast.LENGTH_SHORT).show();
                } else {
                    // 如果sp里有，则删除报错的密码
                    if (Objects.equals(sp.getString("username", null), username)
                            && Objects.equals(sp.getString("password", null), password)) {
                        editor.remove("username");  //删除用户名
                        editor.remove("password");  //删除密码
                        editor.apply();                 //提交更改
                    }
                }

                Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                //开启广告页
                Intent intent = new Intent(this, ActivityAds.class);
                startActivity(intent);
            } else {
                // 登录失败，显示消息并恢复原样
                Toast.makeText(this, "登录失败，请重新输入", Toast.LENGTH_SHORT).show();
            }
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