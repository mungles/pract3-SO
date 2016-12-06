package com.lamejorcompaiadeluniberso.so;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    final int ELEGIR_ARCHIVO_REQUEST_CODE = 42;
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Archivo a = new Archivo(getApplicationContext());
                lineas=a.leerArchivo(uri);
                Toast.makeText(this,"Se han cargado: " + (lineas.size()-1)+" procesos",Toast.LENGTH_LONG).show();
                List<Proceso> procesos = crearProceso(lineas);
                String[] datos = lineas.toArray(new String[lineas.size()-1]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos);
                lista.setAdapter(adapter);

                GestorMemoria gm = new GestorMemoria(procesos);
                gm.procesarComoSiguienteHueco();
            }
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
    }
}
