package com.lamejorcompaiadeluniberso.so;

/**
 * Created by sergio on 7/12/16.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ProcesosAdapter extends BaseAdapter {
    Context ctx;
    List<Proceso> procesos;
    LayoutInflater inflater;

    public ProcesosAdapter(Context ctx, List<Proceso> procesos) {
        this.ctx = ctx;
        this.procesos = procesos;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return procesos.size();
    }

    @Override
    public Object getItem(int i) {
        return procesos.get(i);
    }

    public int getInstante(int i) { return procesos.get(i).getLlegada(); }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static class ViewHolder{
        public TextView item_title, item_content, item_memory, item_time;
        public View item_line;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = inflater.inflate(R.layout.item_main_listview, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.item_title = (TextView) vi.findViewById(R.id.item_title);
        holder.item_content = (TextView) vi.findViewById(R.id.item_content);
        holder.item_line = (View) vi.findViewById(R.id.item_line);
        holder.item_memory = (TextView) vi.findViewById(R.id.item_memory);
        holder.item_time = (TextView) vi.findViewById(R.id.item_time);

        int color = ColorGenerator.getColor(getInstante(i));
        holder.item_line.setBackgroundColor(color);

        if (i == 0 || (getInstante(i) != getInstante(i-1))) {
            holder.item_title.setTextColor(color);
            holder.item_title.setText("Instante " + getInstante(i));
            holder.item_title.setVisibility(View.VISIBLE);
        } else {
            holder.item_title.setVisibility(View.GONE);
        }

        holder.item_memory.setText("\uD83D\uDCBE " + procesos.get(i).getMemoria());
        holder.item_time.setText("\uD83D\uDD51 " + procesos.get(i).getTiempo());
        holder.item_content.setText(procesos.get(i).getNombre());

        vi.setTag(holder);

        return vi;
    }
}
