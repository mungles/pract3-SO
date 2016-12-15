package com.lamejorcompaiadeluniberso.so;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class GraphicsActivity extends AppCompatActivity {
    final int CREATE_REQUEST_CODE = 1000;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graphics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                History.reset();
                finish();
                overridePendingTransition (R.anim.activity_enter, R.anim.activity_out);
                return true;

            case R.id.action_export:
                saveFile();
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

    @Override
    public void onStop() {
        super.onStop();
        overridePendingTransition (R.anim.activity_enter, R.anim.activity_out);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == CREATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final Uri uri = resultData.getData();
            Archivo a = new Archivo(getApplicationContext());
            boolean r = a.escribirFichero(uri, History.getPrintableString());

            if (r) {
                Snackbar snackbar = Snackbar .make(findViewById(R.id.viewpager), "Archivo guardado", Snackbar.LENGTH_LONG);
                snackbar.setAction("Abrir", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File f = new File(uri.getPath());
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setDataAndType(uri, "text/plain");
                        i.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(i);
                    }
                });
                snackbar.show();
            }
        }
    }

    public void saveFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "p3so.txt");

        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }
}
