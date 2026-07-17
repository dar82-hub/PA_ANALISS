package com.analisis.mochila.algoritmo;

public enum ModoAlgoritmo {
    RECURSIVO_EXHAUSTIVO("Recursividad exhaustiva"),
    BACKTRACKING_PODA("Backtracking con poda"),
    COMPARAR("Comparar ambos");

    private final String etiqueta;

    ModoAlgoritmo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
