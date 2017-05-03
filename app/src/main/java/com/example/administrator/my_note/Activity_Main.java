package com.example.administrator.my_note;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Activity_Main extends AppCompatActivity {
    private ImageView img_back;
    private TextView tv_time;
    private EditText edt_content;
    private TextView tv_change;

    String time;
    String content;

    SQLiteDatabase db;
    private ImageView img_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangqing);
        initView();
    }

    private void initView() {
        db = openOrCreateDatabase("MyNote",MODE_PRIVATE,null);
        img_back = (ImageView) findViewById(R.id.btn_back);
        tv_time = (TextView) findViewById(R.id.time);
        edt_content = (EditText) findViewById(R.id.edt_content);
        time = getIntent().getStringExtra("time");
        content = getIntent().getStringExtra("content");
        edt_content.setText(content);
        tv_time.setText(time);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Main.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tv_change = (TextView) findViewById(R.id.btn_finish);
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Activity_Main.this, MainActivity.class);
                String content1 = edt_content.getText().toString();
                String time2 = tv_time.getText().toString();
                db.execSQL("update info set content = '"+content1+"'where content ='"+content+"'");
                db.close();
                intent2.putExtra("content", content);
                intent2.putExtra("time", time);
                startActivity(intent2);
            }
        });

        //分享功能
        img_share = (ImageView) findViewById(R.id.img_share);
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
    }

}
