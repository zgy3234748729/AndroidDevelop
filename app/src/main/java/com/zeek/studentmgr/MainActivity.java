package com.zeek.studentmgr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private static List<String> studentInfo = new ArrayList<>();
    private int editPosition = studentInfo.size();

    private AlertDialog alertDialog;        //弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btnAdd);
        btn.setOnClickListener(view -> {
            toAddStudentView();
        });

        // 获取之前的editPosition值，如果没有则默认为0
        int editPosition = getEditPosition();

        //初始化ListView和适配器
        listView = findViewById(R.id.listView);

        // 创建一个自定义Toast
        LayoutInflater inflater = getLayoutInflater();
        View toastView = inflater.inflate(R.layout.toast, null);
        //设置图标和文本
        ImageView imageView = toastView.findViewById(R.id.iconImageView);
        imageView.setImageResource(R.drawable.ic_icon);     //设置图标
        TextView textView = toastView.findViewById(R.id.messageTextView);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);       //设置持续时间


        //传递进来的值
        String studentMessage = getIntent().getStringExtra("studentMessage");

        if (studentMessage != null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentInfo);
            listView.setAdapter(adapter);
            Toast.makeText(MainActivity.this, "填写学生信息完成,studentInfo=" + studentInfo.size() + "editPosition" + editPosition,
                    Toast.LENGTH_SHORT).show();

            //将接收到的字符串
            for (int i = 0; i < studentInfo.size(); i++) {
                System.out.println(studentInfo.get(i));
            }


            //添加
            if (editPosition >= studentInfo.size()) {
                studentInfo.add(studentMessage);
                textView.setText(getString(R.string.toast_info, "添加", studentInfo.get(editPosition)));
            } else {
                //修改
                studentInfo.set(editPosition, studentMessage);
                textView.setText(getString(R.string.toast_info, "修改", studentInfo.get(editPosition)));
            }
            toast.setView(toastView);
            toast.show();
            adapter.notifyDataSetChanged();

        }


        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            setEditPosition(position);
            int editPosition1 = getEditPosition();
            Toast.makeText(MainActivity.this, "长按问题,editPosition1=" + editPosition1, Toast.LENGTH_SHORT).show();
            registerForContextMenu(listView); // 注册上下文菜单
            openContextMenu(listView); // 打开上下文菜单
            return true;
        });

        //查询学生信息按钮
        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            //创建列表对话框对象
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            //获取LayoutInflater以加载自定义布局文件
            View searchStudentView = inflater.inflate(R.layout.search_student, null);

            //设置自定义布局到对话框
            builder.setView(searchStudentView);
            //设置对话框的其他属性
            builder.setTitle("查询学生");
            builder.setPositiveButton("查询", (dialog, which) -> {
                String name = ((EditText)findViewById(R.id.search_name)).getText().toString();
                String college = ((EditText)findViewById(R.id.search_college)).getText().toString();
                String subject = ((EditText)findViewById(R.id.search_subject)).getText().toString();
                //
            });
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    //
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        assert info != null;
        int position = info.position;
        if (item.getItemId() == R.id.edit_item) {
            //TODO:处理编辑菜单项的点击事件
            Intent intent = new Intent(MainActivity.this, ActivityStudent.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.delete_item) {
            //TODO:用户确认一下
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage("确定要删除下面这个吗\n" + studentInfo);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是",
                    (dialogInterface, i) -> {
                        //TODO:处理删除菜单的点击事件
                        deleteItem(position);
                    });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否",
                    ((dialogInterface, i) -> {
                    }));
            alertDialog.show();
            return true;
        } else {
            return false;
        }
    }

    private void deleteItem(int position) {
        if (position >= 0 && position < studentInfo.size()) {
            studentInfo.remove(position); // 从数据源中删除项
            adapter.notifyDataSetChanged(); // 通知适配器数据已更改
        }
    }

    private int getEditPosition() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("editPosition", studentInfo.size());
    }

    private void setEditPosition(int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("editPosition", position);
        editor.apply();
    }

    //解析菜单文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this); //实例化一个MenuInflater对象
        menuInflater.inflate(R.menu.menu, menu);                    //解析菜单文件
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_item) {
            //TODO:点击增加学生按钮之后
            toAddStudentView();
        } else if (id == R.id.refresh_item) {
            return true;
        }
        return true;
    }

    public void toAddStudentView() {
        Intent intent = new Intent(MainActivity.this, ActivityStudent.class);
        setEditPosition(studentInfo.size());
        startActivity(intent);
    }

    private void showToast() {

    }

}