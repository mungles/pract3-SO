package com.lamejorcompaiadeluniberso.so;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by axel on 01/12/2016.
 */
public class GestorMemoria {
    private final int MAX_MEM = 2000;
    private ArrayList<Particion> particiones = new ArrayList<>();
    private List<Proceso> procesos = new ArrayList<>();

    public GestorMemoria(List<Proceso> procesos) {
        particiones.add(new Particion(0, MAX_MEM, "0", true));
        this.procesos = procesos;
    }

    public List<Proceso> getProcesosEnInstante(int instante) {
        ArrayList<Proceso> proc = new ArrayList<>();

        for (int i = 0; i < procesos.size(); i++) {
            if (procesos.get(i).getLlegada() == instante) {
                proc.add(procesos.get(i));
            }
        }

        return proc;
    }
}
