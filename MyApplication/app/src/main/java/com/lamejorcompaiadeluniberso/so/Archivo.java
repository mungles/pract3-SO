package com.lamejorcompaiadeluniberso.so;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
            String aux;
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

    public boolean escribirFichero(Uri uri, String cadena){
        try {
            ParcelFileDescriptor pfd = ctx.getContentResolver().openFileDescriptor(uri, "w");

            FileOutputStream os = new FileOutputStream(pfd.getFileDescriptor());
            os.write(cadena.getBytes());
            os.close();
            Log.w("P3SO", "File saved in " + uri);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

}
