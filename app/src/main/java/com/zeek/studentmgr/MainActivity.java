package com.zeek.studentmgr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static String TGA = "zeekJet";
    private ListView listView;
    private StudentAdapter adapter;
    private static List<String> studentInfo = new ArrayList<>();
    private int editPosition = studentInfo.size();
    private boolean isSearch = false;
    //设置一个变量来接收查询结果
    private static ArrayList<String> resultList = new ArrayList<>();

    //数据库工具类
    private DBOpenHelper dbOpenHelper;           //定义DBOpenHelper

    private AlertDialog alertDialog;        //弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TGA, "------onCreate------");
        Button btn = findViewById(R.id.btnAdd);
        btn.setOnClickListener(view -> {
            toAddStudentView();
        });

        //新建一个数据库
        dbOpenHelper = new DBOpenHelper(MainActivity.this, "stu.db", null, 1);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

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


        //TODO:主页面需要完成三个功能
        //1：正常情况下显示所有学生，即点击添加学生按钮后，添加完学生，返回来之后，显示所有学生
        ArrayList<String> allStudent = dbOpenHelper.getAllStudentInfo(db);

        //将查询出来的结果让入适配器
        adapter = new StudentAdapter(this, allStudent);
        listView.setAdapter(adapter);
        //通知适配器修改值
        adapter.notifyDataSetChanged();

        //2：TODO:长按listview之后，编辑学生，修改学生在数据库中的值



        //3：点击查询学生之后，更新适配器
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
                String name = ((EditText) searchStudentView.findViewById(R.id.search_name)).getText().toString();
                String college = ((EditText) searchStudentView.findViewById(R.id.search_college)).getText().toString();
                String subject = ((EditText) searchStudentView.findViewById(R.id.search_subject)).getText().toString();
                //查询是否有这样的学生
                String[] info = {name, college, subject};
                resultList = dbOpenHelper.searchStudent(db,info);
                if (resultList.size() == 0) {       ///如果数据库中没有数据
                    //显示提示信息，没有相关记录，不清空listview
                    Toast.makeText(MainActivity.this, "很遗憾，没有相关学生！", Toast.LENGTH_LONG).show();
                } else {
                    //修改适配器，实现查询页面
                    adapter = new StudentAdapter(this, resultList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
            builder.create().show();
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            setEditPosition(position);
            int editPosition1 = getEditPosition();
            Toast.makeText(MainActivity.this, "长按问题,editPosition1=" + editPosition1, Toast.LENGTH_SHORT).show();
            registerForContextMenu(listView); // 注册上下文菜单
            openContextMenu(listView); // 打开上下文菜单
            return true;
        });

        // TODO:显示开发人员信息
        TextView toDeveloperTextview = findViewById(R.id.to_developer_info);
        toDeveloperTextview.setOnClickListener(v -> {
            //跳转到开发者信息页面
            Intent intent = new Intent(MainActivity.this, ActivityAbout.class);
            startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbOpenHelper != null) {
            dbOpenHelper.close();
        }
        Log.d(TGA, "------onDestroy------");
    }

    @Override
    protected  void onStart() {
        super.onStart();
        Log.d(TGA, "------onStart------");
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TGA, "------onResume------");
        // 重新查询数据库获取学生信息并更新ListView
        ArrayList<String> allStudent = dbOpenHelper.getAllStudentInfo(dbOpenHelper.getReadableDatabase());
        adapter = new StudentAdapter(this, allStudent);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected  void onPause() {
        super.onPause();
        Log.d(TGA, "------onPause------");
    }

    @Override
    protected  void onStop() {
        super.onStop();
        Log.d(TGA, "------onStop------");
    }

    @Override
    protected  void onRestart() {
        super.onRestart();
        Log.d(TGA, "------onRestart------");
    }

}