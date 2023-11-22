package com.zeek.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.zeek.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 添加按钮点击事件
        findViewById(R.id.openAboutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建 Intent，指定 Action 和 URI
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.zeek.studentmgr", "com.zeek.studentmgr.ActivityAbout"));
                startActivity(intent);
            }
        });
    }
}