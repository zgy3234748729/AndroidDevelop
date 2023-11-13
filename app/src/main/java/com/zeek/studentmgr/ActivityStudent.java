package com.zeek.studentmgr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class ActivityStudent extends AppCompatActivity {
//    String[] electricItems = getResources().getStringArray(R.array.electric);
    private ArrayAdapter<String> subjectAdapter;
    private DBOpenHelper dbOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //新建数据库引用
        dbOpenHelper = new DBOpenHelper(ActivityStudent.this, "stu.db", null, 1);


        //获取引用
        Spinner collegeSpinner = (Spinner) findViewById(R.id.collegeSpinner);
        Spinner subjectSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        EditText etName = (EditText) findViewById(R.id.name);
        EditText etStudentId = (EditText) findViewById(R.id.studentId);
        RadioGroup sexGroup = (RadioGroup) findViewById(R.id.rg);
        CheckBox h1 = (CheckBox) findViewById(R.id.hobby1);
        CheckBox h2 = (CheckBox) findViewById(R.id.hobby2);
        CheckBox h3 = (CheckBox) findViewById(R.id.hobby3);
        CheckBox h4 = (CheckBox) findViewById(R.id.hobby4);
        Button btn = (Button) findViewById(R.id.btnSubmit);
        DatePicker datePicker = findViewById(R.id.datePicker);      //获取日期组件
        int year = 2003;
        int month = 1;
        int day = 1;
        datePicker.init(year, month - 1, day, null);    //初始化日期组件

        //创建一个默认的专业适配器
        subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);


        collegeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCollege = adapterView.getItemAtPosition(i).toString();
                if (selectedCollege.equals("计算机学院")) {
                    String[] computerItems = getResources().getStringArray(R.array.computer);
                    updateSubjectSpinner(computerItems);
                } else if (selectedCollege.equals("电气学院")) {
                    String[] electricItems = getResources().getStringArray(R.array.electric);
                    updateSubjectSpinner(electricItems);
                } else {
                    subjectAdapter.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //添加学生按钮
        btn.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String studentId = etStudentId.getText().toString();
            //获取性别
            int selectedSexId = sexGroup.getCheckedRadioButtonId();
            RadioButton sexButton = findViewById(selectedSexId);
            String sex = sexButton.getText().toString();
            String college = collegeSpinner.getSelectedItem().toString();
            String subject = subjectSpinner.getSelectedItem().toString();
            boolean hobby1Checked = h1.isChecked();
            boolean hobby2Checked = h2.isChecked();
            boolean hobby3Checked = h3.isChecked();
            boolean hobby4Checked = h4.isChecked();
            //date组件
            int year1 = datePicker.getYear();
            int month1 = datePicker.getMonth();  //月份是从0开始的
            int day1 = datePicker.getDayOfMonth();
            String birthday = year1 + "-" + (month1 + 1) + "-" + day1;

            String studentMessage = "姓名: " + name + "\n" +
                    "学号: " + studentId + "\n" +
                    "性别: " + sex + "\n" +
                    "学院: " + college + "\n" +
                    "专业: " + subject + "\n" +
                    "兴趣爱好: " + (hobby1Checked ? "文学 " : "") +
                    (hobby2Checked ? "体育 " : "") +
                    (hobby3Checked ? "音乐 " : "") +
                    (hobby4Checked ? "美术 " : "") + "\n" +
                    "生日: " + birthday;

            insertData(dbOpenHelper.getReadableDatabase(), name, college, subject, studentMessage);
            //使用Intent传递数据到MainActivity
            Intent intent = new Intent(ActivityStudent.this, MainActivity.class);
//            intent.putExtra("studentMessage",studentMessage);
            startActivity(intent);
        });

    }
    private void updateSubjectSpinner(String[] subjects) {
        //清空专业Spinner
        subjectAdapter.clear();
        //添加新的专业数据
        subjectAdapter.addAll(subjects);
        subjectAdapter.notifyDataSetChanged();
    }

    //插入学生信息
    private void insertData(SQLiteDatabase readableDatabase, String name, String college, String subject, String message) {
        ContentValues values = new ContentValues();
        values.put("name", name);       //保存姓名
        values.put("college", college); //保存学院
        values.put("subject", subject); //专业
        values.put("student_info", message); //完整信息
        readableDatabase.insert("stu", null, values);//执行插入操作
    }
}