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
public class Busqueda_lineal {
    
    private int posicion;
    private int comparaciones;
    private long tiempo;
    private boolean encontrado;

    public Busqueda_lineal() {
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

        for (int i = 0; i < lista.size(); i++) {

            comparaciones++;

            if (lista.get(i).getValor() == valorBuscado) {

                posicion = i;
                encontrado = true;
                break;

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
