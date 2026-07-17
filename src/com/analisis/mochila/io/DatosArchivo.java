package com.analisis.mochila.io;

import com.analisis.mochila.model.ObjetoMochila;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DatosArchivo {
    private final List<ObjetoMochila> objetos;
    private final Integer capacidad;

    public DatosArchivo(List<ObjetoMochila> objetos, Integer capacidad) {
        this.objetos = Collections.unmodifiableList(new ArrayList<>(objetos));
        this.capacidad = capacidad;
    }

    public List<ObjetoMochila> getObjetos() {
        return objetos;
    }

    public Integer getCapacidad() {
        return capacidad;
    }
}
