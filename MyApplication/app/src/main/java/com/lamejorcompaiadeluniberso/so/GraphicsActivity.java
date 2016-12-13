package com.lamejorcompaiadeluniberso.so;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

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

        List<History.Item> items = History.getMoments();
        List<PieDataSet> dataSets = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            List<PieEntry> entries = new ArrayList<>();

            for (int j = 0; j < items.get(i).getParticiones().size(); j++) {
                Particion pa = items.get(i).getParticiones().get(j);
                entries.add(new PieEntry(pa.getTamaño(), pa.getEstado()));
            }

            PieDataSet dataSet = new ColoredPieDataSet(entries, "");
            dataSets.add(dataSet);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(this, dataSets, History.getInstantes()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
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


    public class ColoredPieDataSet extends PieDataSet {
        public ColoredPieDataSet(List<PieEntry> yVals, String label) {
            super(yVals, label);
        }

        @Override
        public int getColor(int index) {
            return ColorGenerator.getColor(index);
        }
    }
}
