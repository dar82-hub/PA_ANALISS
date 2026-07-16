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
public class Shellsort {
    private int comparaciones;
    private int intercambios;

    public Shellsort() {
        comparaciones = 0;
        intercambios = 0;
    }

    public void ordenar(ArrayList<Numero> lista) {

        comparaciones = 0;
        intercambios = 0;

        int n = lista.size();

        for (int salto = n / 2; salto > 0; salto /= 2) {

            for (int i = salto; i < n; i++) {

                Numero aux = lista.get(i);

                int j = i;

                while (j >= salto &&
                        lista.get(j - salto).getValor() > aux.getValor()) {

                    comparaciones++;

                    lista.set(j, lista.get(j - salto));

                    intercambios++;

                    j -= salto;

                }

                lista.set(j, aux);

            }

        }

    }

    public int getComparaciones() {
        return comparaciones;
    }

    public int getIntercambios() {
        return intercambios;
    }

}
