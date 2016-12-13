package com.lamejorcompaiadeluniberso.so;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GraphicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int algoritmo = (int) getIntent().getExtras().get("algoritmo");

        if (algoritmo == 0) {
            getSupportActionBar().setTitle("Siguiente hueco");
        } else if (algoritmo == 1) {
            getSupportActionBar().setTitle("Peor hueco");
        }

            BarChart chart = (BarChart) findViewById(R.id.chart);
            List<History.Item> items = History.getMoments();
            List<BarEntry> entries = new ArrayList<BarEntry>();

            for (int i = 0; i < items.size(); i++) {
                entries.add(new BarEntry(items.get(i).getInstante(), items.get(i).getParticiones().get(0).getTamaño()));
            }

            BarDataSet dataSet = new BarDataSet(entries, "Label"); // add entries to dataset
            dataSet.setColor(ColorGenerator.generateColor());

            BarData lineData = new BarData(dataSet);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
            chart.setData(lineData);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
