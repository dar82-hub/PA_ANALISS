/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author darwi
 */
public class Grafos15_3 {

    public Grafos15_3() {
    }
    
    public String caminosSimples() {

        return "A -> C -> P -> S -> D -> F";

    }

    // b) Camino más corto de C hasta D
    public String caminoMasCorto() {

        return "C -> P -> S -> D";

    }

    // Número de aristas del camino más corto
    public int numeroAristas() {

        return 3;

    }

    // c) ¿Es conexo?
    public boolean esConexo() {

        return true;

    }

    // Información para llenar la JTable
    public Map<String, String> resumen() {

        Map<String, String> datos = new LinkedHashMap<>();

        datos.put("Caminos simples", caminosSimples());
        datos.put("Camino más corto", caminoMasCorto());
        datos.put("Número de aristas", String.valueOf(numeroAristas()));
        datos.put("Grafo conexo", esConexo() ? "SI" : "NO");

        return datos;

    }

    // Texto para el JTextArea
    public String generarResultado() {

        StringBuilder sb = new StringBuilder();

        sb.append("----------------- EJERCICIO 15.3---------------\n\n");

        sb.append("a) Caminos simples de A hasta F\n");
        sb.append(caminosSimples());

        sb.append("\n\n");

        sb.append("b) Camino más corto de C hasta D\n");
        sb.append(caminoMasCorto());

        sb.append("\n");
        sb.append("Numero de aristas: ");
        sb.append(numeroAristas());

        sb.append("\n\n");

        sb.append("c) ¿Es un grafo conexo?\n");

        if (esConexo()) {
            sb.append("SI");
        } else {
            sb.append("NO");
        }

        return sb.toString();

    }
    
}
