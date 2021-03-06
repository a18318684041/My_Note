package com.example.administrator.my_note;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.richeditor.RichEditor;

public class Activity_Main extends AppCompatActivity {
    private ImageView img_back;
    private TextView tv_time;
    private EditText edt_content;
    private TextView tv_change;

    String time;
    String content;

    SQLiteDatabase db;
    private ImageView img_share;

    //图片文字编辑框
    private RichEditor mEditor;
    private String HTML;//图文编辑框的内容


    private LinearLayout photo;
    private LinearLayout take_photo;
    private String path;//图片的地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangqing);
        initView();
    }

    private void initView() {
        photo = (LinearLayout) findViewById(R.id.photo);
        take_photo = (LinearLayout) findViewById(R.id.take_photo);
        db = openOrCreateDatabase("NoteBook", MODE_PRIVATE, null);
        img_back = (ImageView) findViewById(R.id.btn_back);
        tv_time = (TextView) findViewById(R.id.time);
        //edt_content = (EditText) findViewById(R.id.edt_content);

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
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用图片选择器
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1001);
            }
        });
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用系统的照相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1002);
            }
        });

        time = getIntent().getStringExtra("time");

        content = getIntent().getStringExtra("content");
        //edt_content.setText(content);在这开始设置编辑框的内容

        mEditor.requestFocus();
        mEditor.requestFocusFromTouch();
        mEditor.setHtml(content);


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
                String time2 = tv_time.getText().toString();
                //对获取到的HTMl进行判断，如果内容没有改变其值为空
                if(HTML!=null) {
                    db.execSQL("update info set content = '" + HTML + "'where content ='" + content + "'");
                    db.close();
                }
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
/*              //将记事本中的信息分享到QQ、微信等多媒体
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT, edt_content.getText().toString());
                intent1.setType("text/plain");
                startActivity(Intent.createChooser(intent1, "share"));*/
                //分享图文信息
                Uri imageUri = Uri.fromFile(new File(path));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my Share text.");
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "分享到"));
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent2 = new Intent(Activity_Main.this, MainActivity.class);
            //String content1 = edt_content.getText().toString();
            String time2 = tv_time.getText().toString();
            db.execSQL("update info set content = '" + HTML + "'where content ='" + content + "'");
            db.close();
            intent2.putExtra("content", content);
            intent2.putExtra("time", time);
            startActivity(intent2);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
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
                Log.d("AAA", "onActivityResult: " + mEditor.getHtml());
                path = picturePath;
            }
            //调用系统照相机的回调
            else  if(requestCode == 1002 && resultCode == Activity.RESULT_OK){
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                    Log.i("TestFile",
                            "SD card is not avaiable/writeable right now.");
                    return;
                }
                new DateFormat();
                String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                FileOutputStream b = null;
                File file = new File("/sdcard/Image/");
                file.mkdirs();// 创建文件夹
                String fileName = "/sdcard/Image/"+name;

                try {
                    b = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        b.flush();
                        b.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try
                {
                    //将图片插入到编辑器中
                    mEditor.requestFocus();
                    mEditor.requestFocusFromTouch();
                    mEditor.insertImage(fileName,"image");
                }catch(Exception e)
                {
                    Log.e("error", e.getMessage());
                }
            }
        }
    }
}
