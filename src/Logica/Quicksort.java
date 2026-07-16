/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;

/**
 *
 * @author darwi
 */
public class Quicksort {
   private int comparaciones;
    private int intercambios;

    public Quicksort() {
        comparaciones = 0;
        intercambios = 0;
    }

    public void ordenar(ArrayList<Numero> lista) {

        comparaciones = 0;
        intercambios = 0;

        quick(lista, 0, lista.size() - 1);

    }

    private void quick(ArrayList<Numero> lista, int inicio, int fin) {

        if (inicio < fin) {

            int p = particion(lista, inicio, fin);

            quick(lista, inicio, p - 1);

            quick(lista, p + 1, fin);

        }

    }

    private int particion(ArrayList<Numero> lista, int inicio, int fin) {

        Numero pivote = lista.get(fin);

        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {

            comparaciones++;

            if (lista.get(j).getValor() <= pivote.getValor()) {

                i++;

                Numero temp = lista.get(i);

                lista.set(i, lista.get(j));

                lista.set(j, temp);

                intercambios++;

            }

        }

        Numero temp = lista.get(i + 1);

        lista.set(i + 1, lista.get(fin));

        lista.set(fin, temp);

        intercambios++;

        return i + 1;

    }

    public int getComparaciones() {
        return comparaciones;
    }

    public int getIntercambios() {
        return intercambios;
    }
}
