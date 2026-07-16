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
public class ListaNumeros {
    private ArrayList<Numero> lista;

    public ListaNumeros() {
        lista = new ArrayList<>();
    }

    public void agregar(Numero numero) {
        lista.add(numero);
    }

    public ArrayList<Numero> getLista() {
        return lista;
    }

    public void limpiar() {
        lista.clear();
    }

    public int tamaño() {
        return lista.size();
    }

    public Numero obtener(int indice) {
        return lista.get(indice);
    }
}
