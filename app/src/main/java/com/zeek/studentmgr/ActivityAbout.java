package com.zeek.studentmgr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class ActivityAbout extends AppCompatActivity {
    private static String TGA = "Developer_Info";
    TextView phoneNumber;
    TextView emailTextView;
    EditText editTextAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // 获取简介编辑框组件
        editTextAbout = findViewById(R.id.editTextAbout);
        // 获取保存按钮
        Button buttonSave = findViewById(R.id.buttonSave);

        // 为保存按钮设置监听器
        buttonSave.setOnClickListener(v -> {
            saveAboutText();
        });

        // 读取简介并显示在文本输入框中
        String aboutText = readAboutText();
        editTextAbout.setText(aboutText);

        //获取电话号码组件
        phoneNumber = findViewById(R.id.developer_telephone);
        //为电话号码组件设置监听器，实现点击之后打电话
        phoneNumber.setOnClickListener(v -> {
            Log.d(TGA, "电话");
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);       //创建Intent对象
            dialIntent.setData(Uri.parse("tel:18888061675"));
            startActivity(dialIntent);
        });
        //获取邮箱组件
        emailTextView = findViewById(R.id.developer_email);
        //为邮箱设置监听器，实现点击之后发送邮件
        emailTextView.setOnClickListener(v -> {
            Log.d(TGA, "邮箱");

            // 收件人邮箱地址
            String toEmail = "ychunt@qq.com";
            // 主题
            String subject = "3210309013-软件2102-张广园";
            // 创建一个发送邮件的Intent
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto" + toEmail));
            // 设置邮件主题
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            // 启动邮件发送程序
            startActivity(Intent.createChooser(emailIntent, "选择邮件客户端"));
        });
    }

    private void saveAboutText() {
        try {
            String aboutText = editTextAbout.getText().toString();
            FileOutputStream fos = openFileOutput("about", MODE_PRIVATE);
            fos.write(aboutText.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readAboutText() {
        try {
            // 获取文件about的文件输入流
            FileInputStream fis = openFileInput("about");
            // 创建一个输入流读取器，将文件输入流转换为字符输入流
            InputStreamReader isr = new InputStreamReader(fis);
            // 创建一个缓冲读取器，使用字符输入流作为数据源
            BufferedReader br = new BufferedReader(isr);
            // 创建一个StringBuilder对象，用于存储从文件中读取的文本内容
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return  "";
        }
    }
}