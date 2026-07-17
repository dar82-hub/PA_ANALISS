package com.analisis.mochila.model;

import java.util.Objects;

public final class ObjetoMochila {
    private final String nombre;
    private final int peso;
    private final int beneficio;

    public ObjetoMochila(String nombre, int peso, int beneficio) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        if (beneficio < 0) {
            throw new IllegalArgumentException("El beneficio no puede ser negativo.");
        }
        this.nombre = nombre.trim();
        this.peso = peso;
        this.beneficio = beneficio;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPeso() {
        return peso;
    }

    public int getBeneficio() {
        return beneficio;
    }

    @Override
    public String toString() {
        return nombre + " (P: " + peso + ", B: " + beneficio + ")";
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof ObjetoMochila)) {
            return false;
        }
        ObjetoMochila objeto = (ObjetoMochila) otro;
        return peso == objeto.peso
                && beneficio == objeto.beneficio
                && nombre.equals(objeto.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, peso, beneficio);
    }
}
