package com.example.administrator.my_note;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/6 0006.
 */
public class Myadpter_trash_ extends RecyclerView.Adapter<Myadpter_trash_.ViewHolder> {
    private List<String> contents;
    private Context context;

    public Myadpter_trash_(List<String> contents, Context context) {
        this.contents = contents;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trash_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (contents.get(position).length() >= 12) {
            holder.tv.setText(contents.get(position).substring(0, 12) + "...");
        } else {
            holder.tv.setText(contents.get(position));
        }
        holder.ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Main.class);
                intent.setClass(context, Activity_Main.class);
                intent.putExtra("content", contents.get(position));
                Time time = new Time("GMT+8");
                time.setToNow();
                int year = time.year;
                int month = time.month;
                int day = time.monthDay;
                int hour = time.hour;
                int minute = time.minute;
                int second = time.second;
                String now = (month+1) + "月"+day+"日";
                intent.putExtra("time", now);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        LinearLayout ln;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_content);
            ln = (LinearLayout) itemView.findViewById(R.id.ln);
        }
    }
}
