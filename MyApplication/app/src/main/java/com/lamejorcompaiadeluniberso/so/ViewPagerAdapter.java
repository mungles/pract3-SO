package com.lamejorcompaiadeluniberso.so;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergio on 13/12/16.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<PieDataSet> dataSets = new ArrayList<>();
    private List<Integer> instantes = new ArrayList<>();

    public ViewPagerAdapter(Context context, List<PieDataSet> datasets, List<Integer> instantes) {
        mContext = context;
        this.dataSets = datasets;
        this.instantes = instantes;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup child = (ViewGroup) inflater.inflate(R.layout.item_piechart, collection, false);

        PieData data = new PieData(dataSets.get(position));

        PieChart pieChart = (PieChart) child.findViewById(R.id.item_piechart_chart);
        TextView titleTxt = (TextView) child.findViewById(R.id.item_piechart_text);
        titleTxt.setText("Instante " + instantes.get(position));

        pieChart.setData(data);
        pieChart.setRotationEnabled(false);
        pieChart.setTouchEnabled(false);
        pieChart.setDescription(new Description());
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);

        collection.addView(child);
        return child;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return dataSets.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}