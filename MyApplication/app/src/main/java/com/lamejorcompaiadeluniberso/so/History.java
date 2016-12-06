package com.lamejorcompaiadeluniberso.so;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sergio on 4/12/16.
 *
 * Como se usa esta clasecita tan extraña:
 *
 * Guardar un grupo de particiones de un instante concreto:
 * History.addMoment(instante, particiones);
 *
 * Recuperar un grupo de particiones de un instante concreto:
 * History.getMoment(instante);
 *
 * Devuelve un objeto de tipo History.HistoryItem que contiene:
 * - el instante
 * - el List<Particion> de ese instante
 */

public class History {
    private static List<Item> history = new ArrayList<>();
    private static HashMap<Integer, Integer> historyMap = new HashMap<>();

    public static void addMoment(int moment, List<Particion> particiones) {
        if (historyMap.get(moment) == null) {
            history.add(new Item(moment, particiones));
            historyMap.put(moment, history.size() - 1);
        } else {
            history.get(historyMap.get(moment)).updateParticiones(particiones);
        }
    }

    public static List<Item> getMoments() {
        return history;
    }

    public static Item getMoment(int moment) {
        if (historyMap.get(moment) != null) {
            return history.get(historyMap.get(moment));
        } else {
            return null;
        }
    }

    public static List<Particion> getParticionesInMoment(int moment) {
        History.Item i = getMoment(moment);
        if (i != null) { return i.getParticiones(); } else { return null; }
    }

    public static class Item {
        private int instante;
        private List<Particion> particiones;

        public Item(int instante, List<Particion> particiones) {
            this.instante = instante;
            this.particiones = particiones;
        }

        public int getInstante() {
            return instante;
        }

        public List<Particion> getParticiones() {
            return particiones;
        }

        public void updateParticiones(List<Particion> particiones) {
            this.particiones = particiones;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < particiones.size(); i++) {
                sb.append(particiones.get(i).toString());
            }
            return sb.toString();
        }
    }
}
