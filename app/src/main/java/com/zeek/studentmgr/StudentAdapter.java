package com.zeek.studentmgr;

import android.app.PendingIntent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

//设置学生信息显示页面适配器
public class StudentAdapter extends ArrayAdapter<String> {
    private Context context;

    public StudentAdapter(Context context, ArrayList<String> studentInfoList) {
        super(context, R.layout.list_item_student, studentInfoList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_student, parent, false);
        }

        String studentInfo = getItem(position);
        TextView studentInfoTextView = convertView.findViewById(R.id.listview_student_message);
        studentInfoTextView.setText(studentInfo);
        return convertView;
    }
}
