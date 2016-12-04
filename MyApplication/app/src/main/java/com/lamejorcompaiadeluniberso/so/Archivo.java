package com.lamejorcompaiadeluniberso.so;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by axel on 01/12/2016.
 */
public class Archivo {
    Context ctx;

    public Archivo(Context ctx) {
        this.ctx = ctx;
    }

    public boolean ComprobarSD(){
        boolean sdDisponible = false;
        boolean sdAccesoEscritura = false;

        //Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED)){
            sdDisponible = true;
            sdAccesoEscritura = true;
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDisponible = true;
            sdAccesoEscritura = false;
        }
        else {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }

        return sdDisponible;
    }

    public ArrayList<String> leerArchivo(Uri uri){
        try {
            InputStream fin = ctx.getContentResolver().openInputStream(uri);

            BufferedReader r = new BufferedReader(new InputStreamReader(fin));
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = r.readLine()) != null) {
                lines.add(line);
                Log.w("P3SO", line);
            }

            return lines;
        }
        catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
            return null;
        }
    }

}
