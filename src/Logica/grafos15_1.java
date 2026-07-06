/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import Modelo.Grafo;
import java.util.Map;
import Modelo.Nodo;
import java.util.HashMap;

/**
 *
 * @author darwi
 */
public class grafos15_1 {
        private Grafo grafo;


    public grafos15_1() {

        grafo = new Grafo();

        crearGrafo();

    }

    private void crearGrafo() {

        grafo.getVertices().add(new Nodo("H"));
        grafo.getVertices().add(new Nodo("J"));
        grafo.getVertices().add(new Nodo("F"));
        grafo.getVertices().add(new Nodo("L"));
        grafo.getVertices().add(new Nodo("R"));
        grafo.getVertices().add(new Nodo("T"));

        agregarArista("H","J");
        agregarArista("J","F");
        agregarArista("H","F");
        agregarArista("H","T");
        agregarArista("F","L");
        agregarArista("F","R");

    }

    private void agregarArista(String a,String b){

        grafo.getAristas().add(new String[]{a,b});

    }

    public Grafo getGrafo(){

        return grafo;

    }

    public String obtenerVertices(){

        String texto="V = { ";

        for(int i=0;i<grafo.getVertices().size();i++){

            texto+=grafo.getVertices().get(i).getNombre();

            if(i<grafo.getVertices().size()-1){

                texto+=", ";

            }

        }

        texto+=" }";

        return texto;

    }

    public String obtenerAristas(){

        String texto="A = {\n";

        for(String[] a:grafo.getAristas()){

            texto+="("+a[0]+","+a[1]+")\n";

        }

        texto+="}";

        return texto;

    }

    public Map<String,Integer> obtenerGrados(){

        Map<String,Integer> grados=new HashMap<>();

        for(Nodo n:grafo.getVertices()){

            grados.put(n.getNombre(),0);

        }

        for(String[] a:grafo.getAristas()){

            grados.put(a[0],grados.get(a[0])+1);
            grados.put(a[1],grados.get(a[1])+1);

        }

        return grados;

    }
    
}
