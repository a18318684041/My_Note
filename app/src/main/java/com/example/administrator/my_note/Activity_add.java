package com.example.administrator.my_note;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import jp.wasabeef.richeditor.RichEditor;

public class Activity_add extends AppCompatActivity {


    private ImageView img_back;
    private TextView tv_time;
    private TextView tv_finish;
    private EditText edt_content;
    SQLiteDatabase db;

    private String  first_time;
    private String last_time;
    private String content;

    private RecyclerView recyleview;
    private Myadpter adpter;
    private List<String> times = new ArrayList<>();
    private List<String> contents = new ArrayList<>();


    //图片选择，拍照，分享
    private LinearLayout photo;
    private LinearLayout take_photo;
    private  LinearLayout share;
    private ImageView img_share;


    //图片文字编辑框
    private RichEditor mEditor;
    private String HTML;//图文编辑框的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initDatabase();
        initView();
        initGongju();
    }

    private void initGongju() {
        photo = (LinearLayout) findViewById(R.id.photo);
        take_photo = (LinearLayout) findViewById(R.id.take_photo);
        share = (LinearLayout) findViewById(R.id.share);
        img_share = (ImageView) findViewById(R.id.img_share);
        //分享功能
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将记事本中的信息分享到QQ、微信等多媒体
                Intent intent1=new Intent(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT,edt_content.getText().toString());
                intent1.setType("text/plain");
                startActivity(Intent.createChooser(intent1,"share"));
            }
        });
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
        //选择图片
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用系统的照相机
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1001);
            }
        });
    }

    private void initDatabase() {
        //打开数据库
        //db = openOrCreateDatabase("MyNote",MODE_PRIVATE,null);
        db = openOrCreateDatabase("NoteBook",MODE_PRIVATE,null);
    }

    private void initView() {
        img_back = (ImageView) findViewById(R.id.btn_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_add.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tv_time = (TextView) findViewById(R.id.time);
        //获取当前创建的时间,并且设置在输入框的顶部
        NowTime();
        //edt_content = (EditText) findViewById(R.id.edt_content);
        //图文编辑框的初始化
        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(200);//起始编辑设置高度
        mEditor.setEditorFontSize(22);//设置字体大小
        mEditor.setEditorFontColor(Color.RED);//设置字体颜色
        mEditor.setBold();//设置粗体
        mEditor.setItalic();//设置斜体
        //文字变化的监听
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.d("AAA", text);//在这里存储
                HTML = text;
            }
        });

        tv_finish = (TextView) findViewById(R.id.btn_finish);
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time time = new Time("GMT+8");
                time.setToNow();
                int year = time.year;
                int month = time.month;
                int day = time.monthDay;
                int hour = time.hour;
                int minute = time.minute;
                int second = time.second;
                last_time = (month+1) + "月"+day+"日";
                //获取到最后修改的时间
                //content = edt_content.getText().toString();//获取到便签的内容
                Intent intent = new Intent(Activity_add.this,MainActivity.class);
                db.execSQL("insert into info(first_time,last_time,content) values('"+first_time+"','"+last_time+"','"+HTML+"')");
                db.close();
                startActivity(intent);
            }
        });
    }
    private void NowTime() {
        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        int hour = time.hour;
        int minute = time.minute;
        int second = time.second;
        tv_time.setText(year+"-"+(month+1)+"-"+day);
        first_time = year+"-"+(month+1)+"-"+day;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                final String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Log.d("AAA", picturePath);
                //自动获取输入焦点
                mEditor.requestFocus();
                mEditor.requestFocusFromTouch();
                mEditor.insertImage(picturePath, "image");
                Log.d("AAA", "onActivityResult: "+mEditor.getHtml());
            }
        }
    }
}
