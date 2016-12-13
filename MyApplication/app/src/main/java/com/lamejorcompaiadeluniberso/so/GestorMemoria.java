package com.lamejorcompaiadeluniberso.so;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by axel on 01/12/2016.
 */
public class GestorMemoria {
    private final int MAX_MEM = 2000;
    private ArrayList<Particion> particiones = new ArrayList<>();
    private List<Proceso> procesos = new ArrayList<>();

    public GestorMemoria(List<Proceso> procesos) {
        particiones.add(new Particion(0, MAX_MEM, "Libre", true, 0));

        List<Proceso> procesos_copy = new ArrayList<>();
        for (int i = 0; i < procesos.size(); i++) {
            procesos_copy.add(new Proceso(procesos.get(i)));
        }

        this.procesos = procesos_copy;
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

    public void procesarComoSiguienteHueco() {
        int hueco = 0;

        List<Integer> instantes = new ArrayList<>();
        List<Proceso> cola = new ArrayList<>();

        for (int i = 0; i < procesos.size(); i++) {
            if (!instantes.contains(procesos.get(i).getLlegada())) {
                instantes.add(procesos.get(i).getLlegada());
            }
        }

        Log.w("P3SO", instantes.size() + " instantes.");

        for (int i = 0; i < instantes.size(); i++) {
            int current_moment = instantes.get(i);

            List<Proceso> proc = getProcesosEnInstante(current_moment);
            Log.w("P3SO", proc.size() + " procesos en " + current_moment);


            // Controlar ciclo de vida de los procesos que están ya asignados
            for (int j = 0; j < particiones.size(); j++) {
                Particion pa = particiones.get(j);
                if (!pa.isLibre()) {
                    if (pa.getTtl() - current_moment <= 0) {
                        Log.w("P3SO", "Proceso " + pa.getEstado() + " muere.");
                        // El proceso ha muerto en este instante
                        int attatch = -1;
                        if ((j-1) >= 0) {
                            Particion pa_izq = particiones.get(j-1);
                            attatch= pa_izq.isLibre() ? 1 : -1;
                        } else if (j+1 < particiones.size()) {
                            Particion pa_der = particiones.get(j+1);
                            attatch = pa_der.isLibre() ? 0 : -1;
                        }

                        switch (attatch) {
                            case 0:
                                // Juntar con la particion a la derecha
                                Particion pad = particiones.get(j + 1);
                                particiones.get(j).setEstado("Libre");
                                particiones.get(j).setTamaño(pa.getTamaño() + pad.getTamaño());
                                particiones.remove(j+1);
                                particiones.get(j).liberar();
                                break;
                            case 1:
                                // Juntar con la particion a la izquierda
                                particiones.get(j - 1).setEstado("Libre");
                                particiones.get(j - 1).setTamaño(pa.getTamaño() + pa.getTamaño());
                                particiones.remove(j);
                                particiones.get(j - 1).liberar();
                                break;
                            case -1:
                                // Liberar directamente
                                particiones.get(j).setEstado("Libre");
                                particiones.get(j).liberar();
                                break;
                        }
                    }
                }
            }

            // Incorporamos los procesos en cola al principio de la lista de procesos
            for (int j = 0; j < cola.size(); j++) {
                proc.add(j, cola.get(j));
                cola.remove(j);
            }

            // Asignar espacios a procesos
            for (int j = 0; j < proc.size(); j++) {
                Proceso p = proc.get(j);
                Log.w("P3SO", p.toString());
                boolean allocated = false;

                if (hueco >= particiones.size()) { hueco = 0; }

                for (int k = hueco; !allocated && k < particiones.size(); k++) {
                    Particion pa = particiones.get(k);
                    if (pa.isLibre() && p.getMemoria() <= pa.getTamaño()) {
                        int nuevotamaño = pa.getTamaño() - p.getMemoria();
                        int inicio = pa.getInicio();
                        particiones.get(k).setTamaño(nuevotamaño);
                        particiones.get(k).setInicio(pa.getInicio() + p.getMemoria());
                        particiones.add(k, new Particion(inicio, p.getMemoria(), p.getNombre(), false, p.getTiempo() + current_moment));
                        hueco = k;
                        allocated = true;
                        Log.w("P3SO", "Allocated " + p.getNombre() + " in partition " + pa.toString() + "("+k+")");
                    }
                }

                if (!allocated) { cola.add(p); }
            }

            Log.w("P3SO", "Instante " + current_moment + " acaba con " + cola.size() + " procesos en cola.");
            History.addMoment(current_moment, particiones);
        }

        Log.w("P3SO", History.getPrintableString());
    }

    public void procesarComoPeorHueco() {

        History.borraPartida();
        List<Integer> instantes = new ArrayList<>();
        List<Proceso> cola = new ArrayList<>();



        for (int i = 0; i < procesos.size(); i++) {
            if (!instantes.contains(procesos.get(i).getLlegada())) {
                instantes.add(procesos.get(i).getLlegada());
            }
        }
        Log.w("P3SO", instantes.size() + " instantes.");
        for (int i = 0; i < instantes.size(); i++) {
            int tamaño_aux = 0;
            int index_aux=-1;
            List<Proceso> proc = getProcesosEnInstante(instantes.get(i));
            Log.w("P3SO", proc.size() + " procesos en " + instantes.get(i));

            // Controlar ciclo de vida de los procesos que están ya asignados
            for (int j = 0; j < particiones.size(); j++) {
                Particion pa = particiones.get(j);
                if (!pa.isLibre()) {
                    if (pa.getTtl() - instantes.get(i) <= 0) {
                        Log.w("P3SO", "Proceso " + pa.getEstado() + " muere.");
                        // El proceso ha muerto en este instante
                        int attatch = -1;
                        if ((j-1) >= 0) {
                            Particion pa_izq = particiones.get(j-1);
                            attatch= pa_izq.isLibre() ? 1 : -1;
                        } else if (j+1 < particiones.size()) {
                            Particion pa_der = particiones.get(j+1);
                            attatch = pa_der.isLibre() ? 0 : -1;
                        }

                        switch (attatch) {
                            case 0:
                                // Juntar con la particion a la derecha
                                Particion pad = particiones.get(j + 1);
                                particiones.get(j).setEstado("Libre");
                                particiones.get(j).setTamaño(pa.getTamaño() + pad.getTamaño());
                                particiones.remove(j+1);
                                particiones.get(j).liberar();
                                break;
                            case 1:
                                // Juntar con la particion a la izquierda
                                particiones.get(j - 1).setEstado("Libre");
                                particiones.get(j - 1).setTamaño(pa.getTamaño() + pa.getTamaño());
                                particiones.remove(j);
                                particiones.get(j - 1).liberar();
                                break;
                            case -1:
                                // Liberar directamente
                                particiones.get(j).setEstado("Libre");
                                particiones.get(j).liberar();
                                break;
                        }
                    }
                }
            }

            // Incorporamos los procesos en cola al principio de la lista de procesos
            for (int j = 0; j < cola.size(); j++) {
                proc.add(j, cola.get(j));
                cola.remove(j);
            }

            for (int j = 0; j < proc.size(); j++) {
                Proceso p = proc.get(j);
                Log.w("P3SO", p.toString());
                boolean a_troncho = false;

                for(int k=0;k<particiones.size();k++){
                    if(particiones.get(k).getTamaño()>tamaño_aux){
                        tamaño_aux=particiones.get(k).getTamaño();
                        index_aux=k;
                    }
                }

                if(particiones.get(index_aux).getTamaño()>= p.getMemoria() && particiones.get(index_aux).isLibre()){
                    int nuevo_tamaño=particiones.get(index_aux).getTamaño() - p.getMemoria();
                    int nuevo_inicio=particiones.get(index_aux).getInicio();
                    particiones.get(index_aux).setTamaño(nuevo_tamaño);
                    particiones.get(index_aux).setInicio(particiones.get(index_aux).getInicio() + p.getMemoria());
                    particiones.add(index_aux, new Particion(nuevo_inicio, p.getMemoria(), p.getNombre(), false, p.getTiempo() + instantes.get(i)));
                    a_troncho = true;
                    Log.w("P3SO", "Allocated " + p.getNombre() + " in partition " + particiones.get(index_aux).toString() + "("+index_aux+")");
                }

                if (!a_troncho) { cola.add(p); }
            }

            Log.w("P3SO", "Instante " + instantes.get(i) + " acaba con " + cola.size() + " procesos en cola.");
            History.addMoment(instantes.get(i), particiones);
        }
        Log.w("P3SO", History.getPrintableString());
    }
}
