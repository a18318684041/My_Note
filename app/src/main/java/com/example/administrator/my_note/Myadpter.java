package com.example.administrator.my_note;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Myadpter extends RecyclerView.Adapter<Myadpter.ViewHolder> {
    private List<String> contents;
    private List<String> last_time;
    private Context context;
    private List<String> del = new ArrayList<>();
    boolean b = false;

    public Myadpter(List<String> contents, List<String> last_time, Context context) {
        this.contents = contents;
        this.last_time = last_time;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    public void setCheck(boolean a) {
        b = a;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (contents.get(position).length() >= 12) {
            holder.content.setText(contents.get(position).substring(0, 12) + "...");
        } else {
            holder.content.setText(contents.get(position));
        }
        holder.last_time.setText(last_time.get(position));
        holder.ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, Activity_Main.class);
                intent.putExtra("content", contents.get(position));
                intent.putExtra("time", last_time.get(position));
                context.startActivity(intent);
            }
        });
        //设置CheckBox的可见性
        if (b) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
        //设置CheckBox的点击事件
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.checkBox.isChecked()) {
                    del.add(contents.get(position));
                } else {
                    return;
                }
            }
        });

    }

    public List<String> del_content() {
        return del;
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView last_time;
        LinearLayout ln;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            last_time = (TextView) itemView.findViewById(R.id.last_time);
            ln = (LinearLayout) itemView.findViewById(R.id.ln);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
