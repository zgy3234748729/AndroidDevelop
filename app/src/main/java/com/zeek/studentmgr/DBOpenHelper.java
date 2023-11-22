package com.zeek.studentmgr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper {
    // 定义创建数据表stu的SQL语句
    final String CREATE_TABLE_SQL =
            "create table stu(_id integer primary key autoincrement, name, college, subject, student_info)";


    public DBOpenHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version); // 重写构造方法并设置工厂为null
    }

    //获取所有学生的详细信息
    public ArrayList<String> getAllStudentInfo(SQLiteDatabase db) {
        ArrayList<String> resultList = new ArrayList<>();
        // 查询所有学生的SQL语句
        Cursor cursor = db.query("stu", new String[]{"student_info"}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            // 获取每行中的数据
            String studentInfo = cursor.getString(0);
            resultList.add(studentInfo);
        }
        cursor.close();
        return resultList;
    }

    //根据name, college, subject查询学生
    public ArrayList<String> searchStudent(SQLiteDatabase db, String[] searchInfo) {
        ArrayList<String> resultList = new ArrayList<>();
        // SQL语句
        Cursor cursor = db.query("stu", new String[]{"student_info"}, "name = ? AND college = ? AND subject = ? ", searchInfo, null, null, null);
        while (cursor.moveToNext()) {
            //获取每行中的数据
            String studentInfo = cursor.getString(0);
            resultList.add(studentInfo);
        }
        cursor.close();
        return resultList;
    }

    // 增加学生信息

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL); // 创建学生表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 提示版本更新并输出旧版本信息与新版本信息
        System.out.println("---版本更新-----" + oldVersion + "--->" + newVersion);

    }
}
