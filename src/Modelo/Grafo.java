/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;

/**
 *
 * @author darwi
 */
public class Grafo {
    private ArrayList<Nodo> vertices;
    private ArrayList<String[]> aristas;

    public Grafo() {

        vertices = new ArrayList<>();
        aristas = new ArrayList<>();

    }

    public ArrayList<Nodo> getVertices() {
        return vertices;
    }

    public ArrayList<String[]> getAristas() {
        return aristas;
    }

}
