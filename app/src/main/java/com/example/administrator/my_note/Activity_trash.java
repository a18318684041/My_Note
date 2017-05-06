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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;

public class Activity_trash extends AppCompatActivity {

    private ImageView img_back;
    private SQLiteDatabase db;
    private List<String> cotent = new ArrayList<>();
    private RecyclerView recyleview;
    private Myadpter_trash_ adpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        initView();
    }

    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        recyleview = (RecyclerView) findViewById(R.id.recyleview);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_trash.this,MainActivity.class);
                startActivity(intent);
            }
        });
        db = openOrCreateDatabase("Rubish",MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("select * from trash",null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                String con = cursor.getString(cursor.getColumnIndex("content"));
                cotent.add(con);
                Log.d("AAA",con);
            }
            recyleview = (RecyclerView) findViewById(R.id.recyleview);
            adpter = new Myadpter_trash_(cotent, Activity_trash.this);
            LinearLayoutManager manger = new LinearLayoutManager(Activity_trash.this);
            recyleview.setLayoutManager(manger);
            //绘制分割线
            recyleview.addItemDecoration(new DividerItemDecoration(Activity_trash.this, DividerItemDecoration.VERTICAL));
            recyleview.setAdapter(adpter);
            cursor.close();
            db.close();
        }
    }
}

