package com.example.administrator.my_note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class Activity_search extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {


    private MaterialSearchBar searchBar;//搜索栏
    private TextView tv;
    private ImageView img;
    private RecyclerView recyleview;
    private Myadpter_search adpter;

    SQLiteDatabase db;//在数据库中寻找对应的记录
    private List<String> contents;
    private List<String> times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        contents = new ArrayList<>();
        times = new ArrayList<>();
        initSearch();
    }

    private void initSearch() {
        recyleview = (RecyclerView) findViewById(R.id.recyleview);
        tv = (TextView) findViewById(R.id.tv);
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始进行查询记录
                ;
                String input = searchBar.getText().toString();
                Log.d("AAA", input);
                db = openOrCreateDatabase("NoteBook", MODE_PRIVATE, null);
                Cursor cursor = db.rawQuery("select * from info where content ='" + input + "'", null);
                contents.clear();
                times.clear();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String content1 = cursor.getString(cursor.getColumnIndex("content"));
                        String first_time = cursor.getString(cursor.getColumnIndex("first_time"));
                        contents.add(content1);
                        times.add(first_time);
                    }
                    cursor.close();
                    db.close();
                    recyleview = (RecyclerView) findViewById(R.id.recyleview);
                    adpter = new Myadpter_search(contents, times, Activity_search.this);
                    LinearLayoutManager manger = new LinearLayoutManager(Activity_search.this);
                    recyleview.setLayoutManager(manger);
                    //绘制分割线
                    recyleview.addItemDecoration(new DividerItemDecoration(Activity_search.this, DividerItemDecoration.VERTICAL));
                    recyleview.setAdapter(adpter);
                }
            }
        });
        img = (ImageView) findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_search.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}
