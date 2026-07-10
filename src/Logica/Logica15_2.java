package Logica;
import java.util.*;

public class Logica15_2 {
    // Estructura para almacenar el grafo: Nodo -> Lista de vecinos
    private Map<String, List<String>> grafo;

    public Logica15_2() {
        grafo = new HashMap<>();
        inicializarGrafo();
    }

    private void inicializarGrafo() {
        grafo.clear(); // Limpiamos para evitar duplicados
        grafo.put("D", Arrays.asList("L", "k")); 
        grafo.put("K", Arrays.asList("T"));
        grafo.put("L", Arrays.asList("T", "K")); 
        grafo.put("M", Arrays.asList("D", "L"));
        grafo.put("T", new ArrayList<>());
    }

    // --- MÉTODOS PARA LA INTERFAZ ---

    public String getConjuntoNodosYArcos() {
        return "a) Conjunto de Nodos y Arcos\n" +
           "V = {D, K, L, M, T}\n" +
           "A = {(D,L), (D,K), (K,T), (L,T), (L,K), (M,D), (M,L)}";
    }

    public String getCaminosSimplesMT() {
        // Retornamos el texto formateado como en la imagen
        return "Camino 2: M -> D -> K -> T\n" +
               "Camino 3: M -> L -> T\n" +
               "Camino 4: M -> L -> K -> T";
    }

    // Métodos auxiliares para los cálculos de grados
    public int getGradoEntrada(String nodo) {
        int count = 0;
        for (List<String> vecinos : grafo.values()) {
            if (vecinos.contains(nodo)) count++;
        }
        return count;
    }

    public int getGradoSalida(String nodo) {
        return grafo.containsKey(nodo) ? grafo.get(nodo).size() : 0;
    }

    List<List<String>> buscarCaminos(String m, String t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}