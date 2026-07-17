package com.analisis.mochila.io;

import com.analisis.mochila.model.ObjetoMochila;
import com.analisis.mochila.model.ResultadoMochila;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class ArchivoObjetos {
    public DatosArchivo cargar(Path ruta) throws IOException {
        List<ObjetoMochila> objetos = new ArrayList<>();
        Integer capacidad = null;

        try (BufferedReader lector = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
            String linea;
            int numeroLinea = 0;
            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();
                if (linea.isEmpty() || linea.equalsIgnoreCase("nombre;peso;beneficio")) {
                    continue;
                }
                if (linea.toLowerCase().startsWith("# capacidad=")) {
                    capacidad = parsearEntero(
                            linea.substring(linea.indexOf('=') + 1).trim(),
                            "capacidad", numeroLinea);
                    continue;
                }
                if (linea.startsWith("#")) {
                    continue;
                }

                String[] partes = linea.split(";", -1);
                if (partes.length != 3) {
                    throw new IOException("Linea " + numeroLinea
                            + ": use el formato nombre;peso;beneficio.");
                }
                try {
                    objetos.add(new ObjetoMochila(
                            partes[0].trim(),
                            parsearEntero(partes[1].trim(), "peso", numeroLinea),
                            parsearEntero(partes[2].trim(), "beneficio", numeroLinea)));
                } catch (IllegalArgumentException ex) {
                    throw new IOException("Linea " + numeroLinea + ": " + ex.getMessage(), ex);
                }
            }
        }

        if (objetos.isEmpty()) {
            throw new IOException("El archivo no contiene objetos validos.");
        }
        return new DatosArchivo(objetos, capacidad);
    }

    public void guardar(Path ruta, List<ObjetoMochila> objetos, int capacidad)
            throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {
            escritor.write("# capacidad=" + capacidad);
            escritor.newLine();
            escritor.write("nombre;peso;beneficio");
            escritor.newLine();
            for (ObjetoMochila objeto : objetos) {
                escritor.write(objeto.getNombre() + ";"
                        + objeto.getPeso() + ";" + objeto.getBeneficio());
                escritor.newLine();
            }
        }
    }

    public void guardarResultado(
            Path ruta,
            int capacidad,
            List<ObjetoMochila> objetos,
            List<ResultadoMochila> resultados) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {
            escritor.write("REPORTE - PROBLEMA DE LA MOCHILA 0/1");
            escritor.newLine();
            escritor.write("Fecha: " + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            escritor.newLine();
            escritor.write("Capacidad: " + capacidad);
            escritor.newLine();
            escritor.write("Cantidad de objetos: " + objetos.size());
            escritor.newLine();
            escritor.newLine();

            for (ResultadoMochila resultado : resultados) {
                escritor.write("ALGORITMO: " + resultado.getAlgoritmo());
                escritor.newLine();
                escritor.write("Objetos seleccionados:");
                escritor.newLine();
                if (resultado.getSeleccionados().isEmpty()) {
                    escritor.write("  (ninguno)");
                    escritor.newLine();
                } else {
                    for (ObjetoMochila objeto : resultado.getSeleccionados()) {
                        escritor.write("  - " + objeto.getNombre()
                                + " | peso=" + objeto.getPeso()
                                + " | beneficio=" + objeto.getBeneficio());
                        escritor.newLine();
                    }
                }
                escritor.write("Peso total: " + resultado.getPesoTotal());
                escritor.newLine();
                escritor.write("Beneficio maximo: " + resultado.getBeneficioTotal());
                escritor.newLine();
                escritor.write("Llamadas recursivas: " + resultado.getLlamadasRecursivas());
                escritor.newLine();
                escritor.write("Combinaciones evaluadas: "
                        + resultado.getCombinacionesEvaluadas());
                escritor.newLine();
                escritor.write("Ramas podadas: " + resultado.getRamasPodadas());
                escritor.newLine();
                escritor.write(String.format("Tiempo de calculo: %.4f ms",
                        resultado.getTiempoMilisegundos()));
                escritor.newLine();
                escritor.newLine();
            }
        }
    }

    private int parsearEntero(String texto, String campo, int linea) throws IOException {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            throw new IOException("Linea " + linea + ": " + campo
                    + " debe ser un numero entero.", ex);
        }
    }
}
