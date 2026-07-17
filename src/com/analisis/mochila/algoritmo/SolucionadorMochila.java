package com.analisis.mochila.algoritmo;

import com.analisis.mochila.model.ObjetoMochila;
import com.analisis.mochila.model.ResultadoMochila;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class SolucionadorMochila {
    private static final class Estado {
        private List<ObjetoMochila> mejorSeleccion = new ArrayList<>();
        private int mejorPeso;
        private int mejorBeneficio;
        private long llamadas;
        private long combinaciones;
        private long podas;
        private long tiempoEventos;
    }

    public ResultadoMochila resolver(
            List<ObjetoMochila> objetos,
            int capacidad,
            ModoAlgoritmo modo,
            Consumer<EventoAlgoritmo> observador,
            ControlEjecucion control) {
        if (objetos == null || objetos.isEmpty()) {
            throw new IllegalArgumentException("Debe existir al menos un objeto.");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        if (modo == ModoAlgoritmo.COMPARAR) {
            throw new IllegalArgumentException("La comparacion debe ejecutar cada algoritmo por separado.");
        }

        Estado estado = new Estado();
        List<ObjetoMochila> seleccion = new ArrayList<>();
        String nombre = modo.toString();
        long inicio = System.nanoTime();

        emitir(estado, observador, control, TipoEvento.INICIO,
                "Inicia " + nombre + " con capacidad " + capacidad + ".",
                null, 0, 0, 0, seleccion);

        if (modo == ModoAlgoritmo.BACKTRACKING_PODA) {
            int[] beneficioRestante = calcularBeneficioRestante(objetos);
            backtracking(objetos, capacidad, 0, 0, 0, seleccion,
                    beneficioRestante, estado, observador, control);
        } else {
            recursionExhaustiva(objetos, capacidad, 0, 0, 0, seleccion,
                    estado, observador, control);
        }

        emitir(estado, observador, control, TipoEvento.FIN,
                control.isCancelado()
                        ? "Ejecucion detenida por el usuario."
                        : "Finaliza el algoritmo. Mejor beneficio: " + estado.mejorBeneficio + ".",
                null, 0, estado.mejorPeso, estado.mejorBeneficio, estado.mejorSeleccion);

        long duracion = Math.max(0, System.nanoTime() - inicio - estado.tiempoEventos);
        return new ResultadoMochila(
                nombre,
                estado.mejorSeleccion,
                estado.mejorPeso,
                estado.mejorBeneficio,
                estado.llamadas,
                estado.combinaciones,
                estado.podas,
                duracion);
    }

    private void recursionExhaustiva(
            List<ObjetoMochila> objetos,
            int capacidad,
            int indice,
            int peso,
            int beneficio,
            List<ObjetoMochila> seleccion,
            Estado estado,
            Consumer<EventoAlgoritmo> observador,
            ControlEjecucion control) {
        if (control.isCancelado()) {
            return;
        }

        estado.llamadas++;
        ObjetoMochila actual = indice < objetos.size() ? objetos.get(indice) : null;
        emitir(estado, observador, control, TipoEvento.LLAMADA_RECURSIVA,
                actual == null
                        ? "Caso base: se evaluaron todos los objetos."
                        : "Llamada recursiva: evaluar " + actual.getNombre() + ".",
                actual, indice, peso, beneficio, seleccion);

        if (indice == objetos.size()) {
            estado.combinaciones++;
            actualizarMejor(indice, peso, beneficio, seleccion, estado, observador, control);
            return;
        }

        actual = objetos.get(indice);
        if (peso + actual.getPeso() <= capacidad) {
            seleccion.add(actual);
            emitir(estado, observador, control, TipoEvento.INCLUIR,
                    "INCLUIR " + actual.getNombre() + ": se explora la decision 1.",
                    actual, indice, peso + actual.getPeso(),
                    beneficio + actual.getBeneficio(), seleccion);

            recursionExhaustiva(objetos, capacidad, indice + 1,
                    peso + actual.getPeso(), beneficio + actual.getBeneficio(),
                    seleccion, estado, observador, control);

            seleccion.remove(seleccion.size() - 1);
            emitir(estado, observador, control, TipoEvento.BACKTRACKING,
                    "VUELTA ATRAS: se retira " + actual.getNombre() + ".",
                    actual, indice, peso, beneficio, seleccion);
        } else {
            emitir(estado, observador, control, TipoEvento.EXCLUIR,
                    "No se puede incluir " + actual.getNombre()
                            + ": excederia la capacidad.",
                    actual, indice, peso, beneficio, seleccion);
        }

        emitir(estado, observador, control, TipoEvento.EXCLUIR,
                "EXCLUIR " + actual.getNombre() + ": se explora la decision 0.",
                actual, indice, peso, beneficio, seleccion);
        recursionExhaustiva(objetos, capacidad, indice + 1, peso, beneficio,
                seleccion, estado, observador, control);
    }

    private void backtracking(
            List<ObjetoMochila> objetos,
            int capacidad,
            int indice,
            int peso,
            int beneficio,
            List<ObjetoMochila> seleccion,
            int[] beneficioRestante,
            Estado estado,
            Consumer<EventoAlgoritmo> observador,
            ControlEjecucion control) {
        if (control.isCancelado()) {
            return;
        }

        estado.llamadas++;
        ObjetoMochila actual = indice < objetos.size() ? objetos.get(indice) : null;
        emitir(estado, observador, control, TipoEvento.LLAMADA_RECURSIVA,
                actual == null
                        ? "Caso base: se evaluaron todos los objetos."
                        : "Llamada recursiva: evaluar " + actual.getNombre() + ".",
                actual, indice, peso, beneficio, seleccion);

        if (indice == objetos.size()) {
            estado.combinaciones++;
            actualizarMejor(indice, peso, beneficio, seleccion, estado, observador, control);
            return;
        }

        if (beneficio + beneficioRestante[indice] <= estado.mejorBeneficio) {
            estado.podas++;
            emitir(estado, observador, control, TipoEvento.PODA,
                    "PODA: aun tomando todo lo restante no se supera el beneficio "
                            + estado.mejorBeneficio + ".",
                    actual, indice, peso, beneficio, seleccion);
            return;
        }

        if (peso + actual.getPeso() <= capacidad) {
            seleccion.add(actual);
            emitir(estado, observador, control, TipoEvento.INCLUIR,
                    "INCLUIR " + actual.getNombre() + ": se explora la decision 1.",
                    actual, indice, peso + actual.getPeso(),
                    beneficio + actual.getBeneficio(), seleccion);

            backtracking(objetos, capacidad, indice + 1,
                    peso + actual.getPeso(), beneficio + actual.getBeneficio(),
                    seleccion, beneficioRestante, estado, observador, control);

            seleccion.remove(seleccion.size() - 1);
            emitir(estado, observador, control, TipoEvento.BACKTRACKING,
                    "BACKTRACKING: se deshace la seleccion de " + actual.getNombre() + ".",
                    actual, indice, peso, beneficio, seleccion);
        } else {
            estado.podas++;
            emitir(estado, observador, control, TipoEvento.PODA,
                    "PODA POR CAPACIDAD: " + actual.getNombre() + " no cabe.",
                    actual, indice, peso, beneficio, seleccion);
        }

        emitir(estado, observador, control, TipoEvento.EXCLUIR,
                "EXCLUIR " + actual.getNombre() + ": se explora la decision 0.",
                actual, indice, peso, beneficio, seleccion);
        backtracking(objetos, capacidad, indice + 1, peso, beneficio,
                seleccion, beneficioRestante, estado, observador, control);
    }

    private void actualizarMejor(
            int profundidad,
            int peso,
            int beneficio,
            List<ObjetoMochila> seleccion,
            Estado estado,
            Consumer<EventoAlgoritmo> observador,
            ControlEjecucion control) {
        if (beneficio > estado.mejorBeneficio) {
            estado.mejorBeneficio = beneficio;
            estado.mejorPeso = peso;
            estado.mejorSeleccion = new ArrayList<>(seleccion);
            emitir(estado, observador, control, TipoEvento.MEJOR_SOLUCION,
                    "SELECCION OPTIMA PROVISIONAL: nuevo beneficio "
                            + beneficio + " con peso " + peso + ".",
                    null, profundidad, peso, beneficio, seleccion);
        }
    }

    private int[] calcularBeneficioRestante(List<ObjetoMochila> objetos) {
        int[] restante = new int[objetos.size() + 1];
        for (int i = objetos.size() - 1; i >= 0; i--) {
            restante[i] = restante[i + 1] + objetos.get(i).getBeneficio();
        }
        return restante;
    }

    private void emitir(
            Estado estado,
            Consumer<EventoAlgoritmo> observador,
            ControlEjecucion control,
            TipoEvento tipo,
            String mensaje,
            ObjetoMochila objeto,
            int profundidad,
            int peso,
            int beneficio,
            List<ObjetoMochila> seleccion) {
        long inicioEvento = System.nanoTime();
        if (observador != null) {
            observador.accept(new EventoAlgoritmo(
                    tipo, mensaje, objeto, profundidad, peso, beneficio, seleccion));
        }
        control.esperarPaso();
        estado.tiempoEventos += System.nanoTime() - inicioEvento;
    }
}
