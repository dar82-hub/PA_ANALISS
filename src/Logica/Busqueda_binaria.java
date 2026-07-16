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
public class Busqueda_binaria {
     private int posicion;
    private int comparaciones;
    private long tiempo;
    private boolean encontrado;

    public Busqueda_binaria() {

        posicion = -1;
        comparaciones = 0;
        tiempo = 0;
        encontrado = false;

    }

    public void buscar(ArrayList<Numero> lista, int valorBuscado) {

        posicion = -1;
        comparaciones = 0;
        encontrado = false;

        long inicio = System.nanoTime();

        int izquierda = 0;
        int derecha = lista.size() - 1;

        while (izquierda <= derecha) {

            comparaciones++;

            int centro = (izquierda + derecha) / 2;

            if (lista.get(centro).getValor() == valorBuscado) {

                posicion = centro;
                encontrado = true;
                break;

            }

            if (lista.get(centro).getValor() < valorBuscado) {

                izquierda = centro + 1;

            } else {

                derecha = centro - 1;

            }

        }

        long fin = System.nanoTime();

        tiempo = fin - inicio;

    }

    public int getPosicion() {
        return posicion;
    }

    public int getComparaciones() {
        return comparaciones;
    }

    public long getTiempo() {
        return tiempo;
    }

    public boolean isEncontrado() {
        return encontrado;
    }
}
