package com.example.administrator.my_note;

import android.content.Context;
import android.content.Intent;
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

/**
 * Created by Administrator on 2017/5/3 0003.
 */
public class Myadpter_search extends RecyclerView.Adapter<Myadpter_search.ViewHolder>{
    private List<String> contents;
    private List<String> last_time;
    private Context context;


    public Myadpter_search(List<String> contents, List<String> last_time, Context context) {
        this.contents = contents;
        this.last_time = last_time;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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
    }

    @Override
    public int getItemCount() {
        return last_time.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView last_time;
        LinearLayout ln;
        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            last_time = (TextView) itemView.findViewById(R.id.last_time);
            ln = (LinearLayout) itemView.findViewById(R.id.ln);
        }
    }
}
