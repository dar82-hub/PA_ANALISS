package com.analisis.mochila.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ResultadoMochila {
    private final String algoritmo;
    private final List<ObjetoMochila> seleccionados;
    private final int pesoTotal;
    private final int beneficioTotal;
    private final long llamadasRecursivas;
    private final long combinacionesEvaluadas;
    private final long ramasPodadas;
    private final long tiempoNanosegundos;

    public ResultadoMochila(
            String algoritmo,
            List<ObjetoMochila> seleccionados,
            int pesoTotal,
            int beneficioTotal,
            long llamadasRecursivas,
            long combinacionesEvaluadas,
            long ramasPodadas,
            long tiempoNanosegundos) {
        this.algoritmo = algoritmo;
        this.seleccionados = Collections.unmodifiableList(new ArrayList<>(seleccionados));
        this.pesoTotal = pesoTotal;
        this.beneficioTotal = beneficioTotal;
        this.llamadasRecursivas = llamadasRecursivas;
        this.combinacionesEvaluadas = combinacionesEvaluadas;
        this.ramasPodadas = ramasPodadas;
        this.tiempoNanosegundos = tiempoNanosegundos;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public List<ObjetoMochila> getSeleccionados() {
        return seleccionados;
    }

    public int getPesoTotal() {
        return pesoTotal;
    }

    public int getBeneficioTotal() {
        return beneficioTotal;
    }

    public long getLlamadasRecursivas() {
        return llamadasRecursivas;
    }

    public long getCombinacionesEvaluadas() {
        return combinacionesEvaluadas;
    }

    public long getRamasPodadas() {
        return ramasPodadas;
    }

    public long getTiempoNanosegundos() {
        return tiempoNanosegundos;
    }

    public double getTiempoMilisegundos() {
        return tiempoNanosegundos / 1_000_000.0;
    }
}
