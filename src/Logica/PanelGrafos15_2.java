package Logica;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class PanelGrafos15_2 extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        // Coordenadas (x, y) calculadas para formar el grafo circular
        int d = 50; // diámetro
        int xD=175, yD=50;  // D arriba
        int xK=325, yK=50;  // K derecha-arriba
        int xL=325, yL=200; // L derecha-abajo
        int xM=175, yM=300; // M abajo
        int xT=25,  yT=200; // T izquierda

        // 1. Dibujar Arcos (Flechas) - Ajusta las coordenadas según el ejercicio
        // Usamos una función para dibujar flechas con punta
        dibujarFlecha(g2, xD, yD, xL, yL, d); // D -> L
        dibujarFlecha(g2, xD, yD, xK, yK, d); // D -> T

        // 3. Mantenemos las conexiones de K y otros
        dibujarFlecha(g2, xK, yK, xT, yT, d); // K -> T

        // 4. Mantenemos las conexiones de M
        dibujarFlecha(g2, xM, yM, xD, yD, d); // M -> D
        
        dibujarFlecha(g2, xM, yM, xL, yL, d); // M -> L

        // 5. Mantenemos la conexión de L -> T y agregamos L -> K
        dibujarFlecha(g2, xL, yL, xT, yT, d); // L -> T
        dibujarFlecha(g2, xL, yL, xK, yK, d); // L -> K  <-- ¡NUEVA CONEXIÓN!

        // 2. Dibujar Nodos (Círculos)
        dibujarNodo(g2, "D", xD, yD, d, Color.BLUE);
        dibujarNodo(g2, "K", xK, yK, d, Color.RED);
        dibujarNodo(g2, "L", xL, yL, d, Color.GREEN);
        dibujarNodo(g2, "M", xM, yM, d, Color.YELLOW);
        dibujarNodo(g2, "T", xT, yT, d, Color.MAGENTA);
    }
    private String nodoResaltado = "";

    public void resaltarNodo(String nodo) {
        this.nodoResaltado = nodo;
        repaint();
    }

    private void dibujarNodo(Graphics2D g, String nombre, int x, int y, int d, Color color) {
        g.setColor(color);
        g.fillOval(x, y, d, d);

        // Si es el nodo resaltado, dibujamos un borde grueso (CYAN)
        if (nombre.equals(nodoResaltado)) {
            g.setColor(Color.CYAN);
            g.setStroke(new BasicStroke(5)); // Borde grueso
            g.drawOval(x, y, d, d);
            g.setStroke(new BasicStroke(2)); // Volvemos al grosor normal
        } else {
            g.setColor(Color.BLACK);
            g.drawOval(x, y, d, d);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(nombre, x + (d/3), y + (d/2) + 5);
    }

    private void dibujarFlecha(Graphics2D g, int x1, int y1, int x2, int y2, int d) {
    // 1. Calcular puntos centrales de los círculos (nodos)
    int cx1 = x1 + d / 2;
    int cy1 = y1 + d / 2;
    int cx2 = x2 + d / 2;
    int cy2 = y2 + d / 2;

    // 2. Ajustar para que la línea termine en el borde del círculo, no en el centro
    double angulo = Math.atan2(cy2 - cy1, cx2 - cx1);
    int xFin = (int) (cx2 - (d / 2) * Math.cos(angulo));
    int yFin = (int) (cy2 - (d / 2) * Math.sin(angulo));

    // 3. Dibujar la línea principal
    g.setColor(Color.BLACK);
    g.drawLine(cx1, cy1, xFin, yFin);

    // 4. Dibujar la punta de la flecha
    double anguloPunta = Math.toRadians(30); // 30 grados de apertura
    int longitudPunta = 15;
    
    // Calcular las dos líneas de la punta
    int xPunta1 = (int) (xFin - longitudPunta * Math.cos(angulo - anguloPunta));
    int yPunta1 = (int) (yFin - longitudPunta * Math.sin(angulo - anguloPunta));
    int xPunta2 = (int) (xFin - longitudPunta * Math.cos(angulo + anguloPunta));
    int yPunta2 = (int) (yFin - longitudPunta * Math.sin(angulo + anguloPunta));

    g.drawLine(xFin, yFin, xPunta1, yPunta1);
    g.drawLine(xFin, yFin, xPunta2, yPunta2);
}
}