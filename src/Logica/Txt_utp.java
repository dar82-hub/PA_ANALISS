/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author darwi
 */
public class Txt_utp {
   public static void cargarArchivo(String ruta,
                                     ListaNumeros lista)
            throws IOException {

        lista.limpiar();

        BufferedReader br = new BufferedReader(
                new FileReader(ruta)
        );

        String linea;

        while ((linea = br.readLine()) != null) {

            linea = linea.trim();

            if (!linea.isEmpty()) {

                lista.agregar(
                        new Numero(
                                Integer.parseInt(linea)
                        )
                );

            }

        }

        br.close();

    }
}
