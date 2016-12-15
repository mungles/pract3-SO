package com.lamejorcompaiadeluniberso.so;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.InflateException;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pl.droidsonroids.gif.GifImageView;

import static android.R.attr.button;
import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    final int ELEGIR_ARCHIVO_REQUEST_CODE = 42;
    ListView lista;
    List<Proceso> procesos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gestión de memoria");


        lista = (ListView)findViewById(R.id.list_view);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_upload) {
            elegirArchivo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        List<String> lineas = new ArrayList<String>();
        int memoria=0;
        if (requestCode == ELEGIR_ARCHIVO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                String uri_s = uri.toString() + "";
                String uri_p[] = uri_s.split("%3A");
                getSupportActionBar().setSubtitle(uri_p[uri_p.length-1]);

                Archivo a = new Archivo(getApplicationContext());
                lineas=a.leerArchivo(uri);
                Toast.makeText(this,"Se han cargado: " + (lineas.size()-1) +" procesos",Toast.LENGTH_LONG).show();
                procesos = crearProceso(lineas);
                String[] datos = lineas.toArray(new String[lineas.size()-1]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos);

                //lista.setAdapter(adapter);
                lista.setDivider(null);
                lista.setDividerHeight(0);
                lista.setAdapter(new ProcesosAdapter(getApplicationContext(), procesos));

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                        fab.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open));
                        fab.setVisibility(View.VISIBLE);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lanzarMenu(view);
                            }
                        });
                    }
                }, 300);
            }
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);
        menu.setHeaderTitle("Seleccione algoritmo");
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.opciones,menu);
    }

    public boolean onContextItemSelected(MenuItem item){
        Window window = getWindow();
        String cadena;
        switch (item.getItemId()){
            case R.id.siguiente_hueco:
                item.setChecked(true);

                Intent i = new Intent(MainActivity.this, GraphicsActivity.class);
                i.putExtra("algoritmo", 0);

                GestorMemoria gm = new GestorMemoria(procesos);
                cadena=gm.procesarComoSiguienteHueco();
                startActivity(i);
                overridePendingTransition (R.anim.activity_enter, R.anim.activity_out);

                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);

                return true;
            case R.id.peor_hueco:
                item.setChecked(true);

                GestorMemoria GM = new GestorMemoria(procesos);
                cadena=GM.procesarComoPeorHueco();
                Intent in = new Intent(MainActivity.this, GraphicsActivity.class);
                in.putExtra("algoritmo", 1);

                startActivity(in);
                overridePendingTransition (R.anim.activity_enter, R.anim.activity_out);

                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    public void lanzarMenu(final View v){
        try {
            registerForContextMenu(v);
            openContextMenu(v);
        } catch (InflateException ie) {
            ie.printStackTrace();
        }
    }

    private List<Proceso> crearProceso(List<String> s){
        List<String> linea;
        List<Proceso> procesos = new ArrayList<Proceso>();
        linea = s;


        for(int i=0;i<linea.size();i++){
            String[] parts = linea.get(i).split(" ");
            Proceso p = new Proceso(parts[0],Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),Integer.parseInt(parts[3]));
            procesos.add(p);
            if (parts[0].toLowerCase().equals("arcoiris")) {
                FantasticRainbowFun();
            }
        }

        /*for(int i=0;i<procesos.size();i++){
            Log.w("P3SO",procesos.get(i).toString());
        }*/

        return procesos;

    }

    private void elegirArchivo() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, ELEGIR_ARCHIVO_REQUEST_CODE);
        findViewById(R.id.introlayout).setVisibility(View.GONE);
    }

    private void FantasticRainbowFun() {
        GifImageView gv = (GifImageView) findViewById(R.id.gif);
        gv.setVisibility(View.VISIBLE);

        GifImageView gv2 = (GifImageView) findViewById(R.id.gif2);
        gv2.setVisibility(View.VISIBLE);

        superloop();

        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.taste);
        mp.start();
    }

    private void superloop() {
        int color = ColorGenerator.generateColor();
        (findViewById(R.id.toolbar)).setBackgroundColor(color);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                superloop();
            }
        }, 100);
    }
}
