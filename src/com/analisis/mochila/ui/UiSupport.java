package com.analisis.mochila.ui;

import com.analisis.mochila.algoritmo.TipoEvento;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicButtonUI;

public final class UiSupport {
    public static final Color FONDO = new Color(246, 249, 252);
    public static final Color SUPERFICIE = Color.WHITE;
    public static final Color TEXTO = new Color(32, 45, 55);
    public static final Color BORDE = new Color(194, 207, 218);
    public static final Color AZUL = new Color(11, 91, 162);
    public static final Color AZUL_CLARO = new Color(222, 239, 252);
    public static final Color VERDE = new Color(0, 121, 83);
    public static final Color VERDE_CLARO = new Color(218, 245, 232);
    public static final Color NARANJA = new Color(181, 92, 0);
    public static final Color NARANJA_CLARO = new Color(255, 236, 209);
    public static final Color ROJO = new Color(196, 26, 38);
    public static final Color ROJO_CLARO = new Color(255, 224, 226);
    public static final Color MORADO = new Color(102, 45, 145);
    public static final Color MORADO_CLARO = new Color(238, 228, 249);
    public static final Color GRIS = new Color(74, 88, 99);
    public static final Color GRIS_CLARO = new Color(232, 238, 243);

    private UiSupport() {
    }

    public static void aplicarEstiloGlobal() {
        FontUIResource fuente = new FontUIResource("SansSerif", Font.PLAIN, 14);
        Enumeration<Object> claves = UIManager.getDefaults().keys();
        while (claves.hasMoreElements()) {
            Object clave = claves.nextElement();
            Object valor = UIManager.get(clave);
            if (valor instanceof FontUIResource) {
                UIManager.put(clave, fuente);
            }
        }
        UIManager.put("OptionPane.messageFont", fuente);
        UIManager.put("OptionPane.buttonFont", fuente);
        UIManager.put("TableHeader.font", new FontUIResource("SansSerif", Font.BOLD, 14));
    }

    public static JButton crearBoton(String texto, Color fondo, Color frente) {
        JButton boton = new JButton(texto);
        boton.setUI(new BasicButtonUI());
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setFocusPainted(false);
        boton.setBorderPainted(true);
        boton.setBackground(fondo);
        boton.setForeground(frente);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fondo.darker()),
                new EmptyBorder(8, 12, 8, 12)));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    public static TitledBorder crearBordeTitulado(String titulo) {
        TitledBorder borde = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDE), titulo);
        borde.setTitleFont(new Font("SansSerif", Font.BOLD, 14));
        borde.setTitleColor(new Color(42, 75, 96));
        return borde;
    }

    public static Color colorEvento(TipoEvento tipo) {
        switch (tipo) {
            case INCLUIR:
                return VERDE;
            case EXCLUIR:
                return NARANJA;
            case PODA:
                return ROJO;
            case MEJOR_SOLUCION:
                return MORADO;
            case BACKTRACKING:
                return GRIS;
            case INICIO:
            case FIN:
                return AZUL;
            default:
                return new Color(0, 76, 153);
        }
    }

    public static Color fondoEvento(TipoEvento tipo) {
        switch (tipo) {
            case INCLUIR:
                return VERDE_CLARO;
            case EXCLUIR:
                return NARANJA_CLARO;
            case PODA:
                return ROJO_CLARO;
            case MEJOR_SOLUCION:
                return MORADO_CLARO;
            case BACKTRACKING:
                return GRIS_CLARO;
            default:
                return AZUL_CLARO;
        }
    }

    public static String etiquetaEvento(TipoEvento tipo) {
        switch (tipo) {
            case LLAMADA_RECURSIVA:
                return "[LLAMADA]";
            case INCLUIR:
                return "[INCLUIR]";
            case EXCLUIR:
                return "[EXCLUIR]";
            case BACKTRACKING:
                return "[VOLVER]";
            case PODA:
                return "[PODA]";
            case MEJOR_SOLUCION:
                return "[MEJOR]";
            case INICIO:
                return "[INICIO]";
            case FIN:
                return "[FIN]";
            default:
                return "[PASO]";
        }
    }
}
