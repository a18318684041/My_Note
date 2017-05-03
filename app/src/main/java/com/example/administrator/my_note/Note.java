package com.example.administrator.my_note;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class Note {
    private String first_time;//创建时间
    private String last_time; //最后修改的时间
    private String content;   //文本的内容
    public Note(String first_time, String last_time, String content) {
        this.first_time = first_time;
        this.last_time = last_time;
        this.content = content;
    }
}
