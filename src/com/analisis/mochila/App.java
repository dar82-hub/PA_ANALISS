package com.analisis.mochila;

import com.analisis.mochila.ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Swing usara su apariencia multiplataforma si el sistema no esta disponible.
            }
            MainFrame.aplicarEstiloGlobal();
            new MainFrame().setVisible(true);
        });
    }
}
