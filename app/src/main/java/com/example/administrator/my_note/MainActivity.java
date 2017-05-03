package com.example.administrator.my_note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private ImageView img_add;
    SQLiteDatabase db;
    private Button btn_del;

    private RecyclerView recyleview;
    private Myadpter adpter;
    private List<String> times = new ArrayList<>();
    private List<String> contents = new ArrayList<>();
    private TextView tv_edt;
    private TextView tv_cancel;

    private MaterialSearchBar searchBar;//搜索栏
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //初始化搜索栏
        initSearch();
    }

    private void initSearch() {
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);
    }

    private void initView() {
        img_add = (ImageView) findViewById(R.id.img_add);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Activity_add.class);
                startActivity(intent);
            }
        });

        btn_del = (Button) findViewById(R.id.btn_del);
        //删除记录
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* db.execSQL("delete from info content ='你好1'" );*/
                List<String> a = adpter.del_content();
                db = openOrCreateDatabase("MyNote",MODE_PRIVATE,null);;
                db.execSQL("CREATE TABLE IF NOT EXISTS info (first_time varchar(10), last_time varchar(10), content varchar(50))");
                Log.d("AAA", a.size()+"'");
                for(int i = 0;i<a.size();i++){
                    Log.d("AAA","delete from info where content ='"+a.get(i)+"'");
                    db.execSQL("delete from info where content = ?",new String[]{a.get(i)});
                }
                Cursor cursor = db.rawQuery("select * from info",null);
                List<String> con = new ArrayList<String>();
                List<String> first = new ArrayList<String>();
                if(cursor!=null){
                    while(cursor.moveToNext()){
                        String content1 = cursor.getString(cursor.getColumnIndex("content"));
                        String first_time = cursor.getString(cursor.getColumnIndex("first_time"));
                        con.add(content1);
                        first.add(first_time);
                    }
                    recyleview = (RecyclerView) findViewById(R.id.recyleview);
                    adpter = new Myadpter(con, first, MainActivity.this);
                    LinearLayoutManager manger = new LinearLayoutManager(MainActivity.this);
                    recyleview.setLayoutManager(manger);
                    //绘制分割线
                    recyleview.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
                    recyleview.setAdapter(adpter);
                    cursor.close();
                    db.close();
                    tv_edt.setVisibility(View.VISIBLE);
                    tv_cancel.setVisibility(View.GONE);
                    img_add.setVisibility(View.VISIBLE);
                    adpter.setCheck(false);
                    adpter.notifyDataSetChanged();
                    btn_del.setVisibility(View.GONE);
                }
            }
        });

        tv_cancel = (TextView) findViewById(R.id.cancel);
        tv_edt = (TextView) findViewById(R.id.tv_edt);
        tv_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edt.setVisibility(View.INVISIBLE);
                tv_cancel.setVisibility(View.VISIBLE);
                img_add.setVisibility(View.INVISIBLE);
                adpter.setCheck(true);
                adpter.notifyDataSetChanged();
                btn_del.setVisibility(View.VISIBLE);

            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edt.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.GONE);
                img_add.setVisibility(View.VISIBLE);
                adpter.setCheck(false);
                adpter.notifyDataSetChanged();
                btn_del.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        db = openOrCreateDatabase("MyNote",MODE_PRIVATE,null);;
        db.execSQL("CREATE TABLE IF NOT EXISTS info (first_time varchar(10), last_time varchar(10), content varchar(50))");
        Cursor cursor = db.rawQuery("select * from info",null);
        contents.clear();
        times.clear();
        if(cursor!=null){
            while(cursor.moveToNext()){
                String content1 = cursor.getString(cursor.getColumnIndex("content"));
                String first_time = cursor.getString(cursor.getColumnIndex("first_time"));
                contents.add(content1);
                times.add(first_time);
            }
            cursor.close();
            db.close();
            recyleview = (RecyclerView) findViewById(R.id.recyleview);
            adpter = new Myadpter(contents, times, MainActivity.this);
            LinearLayoutManager manger = new LinearLayoutManager(MainActivity.this);
            recyleview.setLayoutManager(manger);
            //绘制分割线
            recyleview.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
            recyleview.setAdapter(adpter);
        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        String s = enabled ? "enabled" : "disabled";
        if(enabled){
            //跳转到搜索页面
            Intent intent = new Intent(MainActivity.this,Activity_search.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}
