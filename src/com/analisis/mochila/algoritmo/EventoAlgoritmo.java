package com.analisis.mochila.algoritmo;

import com.analisis.mochila.model.ObjetoMochila;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EventoAlgoritmo {
    private final TipoEvento tipo;
    private final String mensaje;
    private final ObjetoMochila objeto;
    private final int profundidad;
    private final int pesoActual;
    private final int beneficioActual;
    private final List<ObjetoMochila> seleccionActual;

    public EventoAlgoritmo(
            TipoEvento tipo,
            String mensaje,
            ObjetoMochila objeto,
            int profundidad,
            int pesoActual,
            int beneficioActual,
            List<ObjetoMochila> seleccionActual) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.objeto = objeto;
        this.profundidad = profundidad;
        this.pesoActual = pesoActual;
        this.beneficioActual = beneficioActual;
        this.seleccionActual = Collections.unmodifiableList(new ArrayList<>(seleccionActual));
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public ObjetoMochila getObjeto() {
        return objeto;
    }

    public int getProfundidad() {
        return profundidad;
    }

    public int getPesoActual() {
        return pesoActual;
    }

    public int getBeneficioActual() {
        return beneficioActual;
    }

    public List<ObjetoMochila> getSeleccionActual() {
        return seleccionActual;
    }
}
