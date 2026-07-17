package com.analisis.mochila.ui;

import com.analisis.mochila.io.ArchivoObjetos;
import com.analisis.mochila.model.ObjetoMochila;
import com.analisis.mochila.model.ResultadoMochila;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public final class ResultadoFrame extends JFrame {
    private static final Color FONDO_ELEGANTE = new Color(238, 246, 250);
    private static final Color BANDA = new Color(214, 233, 242);
    private static final Color ACENTO = new Color(0, 116, 132);
    private static final Color ACENTO_SUAVE = new Color(219, 244, 240);
    private static final Color DORADO_SUAVE = new Color(255, 244, 214);

    private final List<ObjetoMochila> objetos;
    private final int capacidad;
    private final List<ResultadoMochila> resultados;
    private final ArchivoObjetos archivoObjetos = new ArchivoObjetos();

    public ResultadoFrame(JFrame padre, List<ObjetoMochila> objetos, int capacidad, List<ResultadoMochila> resultados) {
        super("Resultado final - Seleccion optima");
        this.objetos = new ArrayList<>(objetos);
        this.capacidad = capacidad;
        this.resultados = new ArrayList<>(resultados);
        construirInterfaz();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(880, 560));
        setSize(980, 650);
        setLocationRelativeTo(padre);
    }

    private void construirInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBackground(FONDO_ELEGANTE);
        raiz.setBorder(new EmptyBorder(14, 16, 14, 16));
        setContentPane(raiz);

        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        raiz.add(crearCentro(), BorderLayout.CENTER);
        raiz.add(crearBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearEncabezado() {
        ResultadoMochila mejor = obtenerMejor();
        JPanel panel = new JPanel(new BorderLayout(12, 8));
        panel.setBackground(BANDA);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(164, 205, 219)),
                new EmptyBorder(14, 18, 14, 18)));

        JLabel titulo = new JLabel("SOLUCION OPTIMA ENCONTRADA");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(new Color(18, 79, 98));
        JLabel subtitulo = new JLabel("La mejor seleccion respeta la capacidad y maximiza el beneficio.");
        subtitulo.setForeground(UiSupport.TEXTO);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 4));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);
        panel.add(textos, BorderLayout.WEST);

        JPanel metricas = new JPanel(new GridLayout(1, 3, 8, 0));
        metricas.setOpaque(false);
        metricas.add(crearTarjetaMetrica("Beneficio maximo", String.valueOf(mejor.getBeneficioTotal()), ACENTO_SUAVE));
        metricas.add(crearTarjetaMetrica("Peso total", mejor.getPesoTotal() + " / " + capacidad, DORADO_SUAVE));
        metricas.add(crearTarjetaMetrica("Objetos", String.valueOf(mejor.getSeleccionados().size()), UiSupport.MORADO_CLARO));
        panel.add(metricas, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearCentro() {
        JPanel centro = new JPanel(new GridLayout(2, 1, 0, 10));
        centro.setBackground(FONDO_ELEGANTE);
        centro.add(crearPanelSeleccion());
        centro.add(crearPanelComparacion());
        return centro;
    }

    private JPanel crearPanelSeleccion() {
        ResultadoMochila mejor = obtenerMejor();
        List<String> filas = new ArrayList<>();
        if (mejor.getSeleccionados().isEmpty()) {
            filas.add("(No se seleccionaron objetos)");
        } else {
            for (ObjetoMochila objeto : mejor.getSeleccionados()) {
                filas.add(objeto.getNombre() + "  |  peso: " + objeto.getPeso()
                        + "  |  beneficio: " + objeto.getBeneficio());
            }
        }
        JList<String> lista = new JList<>(filas.toArray(new String[0]));
        lista.setFixedCellHeight(32);
        lista.setBackground(UiSupport.SUPERFICIE);
        lista.setForeground(UiSupport.TEXTO);
        lista.setBorder(new EmptyBorder(6, 8, 6, 8));
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel panel = crearContenedor("Mejor combinacion seleccionada");
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelComparacion() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Algoritmo", "Beneficio", "Peso", "Llamadas", "Combinaciones", "Podas", "Tiempo (ms)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (ResultadoMochila resultado : resultados) {
            modelo.addRow(new Object[]{
                resultado.getAlgoritmo(),
                resultado.getBeneficioTotal(),
                resultado.getPesoTotal(),
                resultado.getLlamadasRecursivas(),
                resultado.getCombinacionesEvaluadas(),
                resultado.getRamasPodadas(),
                String.format("%.4f", resultado.getTiempoMilisegundos())
            });
        }
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(32);
        tabla.setGridColor(new Color(210, 225, 233));
        tabla.setSelectionBackground(ACENTO_SUAVE);
        tabla.setSelectionForeground(UiSupport.TEXTO);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setBackground(new Color(225, 238, 245));
        tabla.getTableHeader().setForeground(new Color(31, 72, 91));
        tabla.setDefaultRenderer(Object.class, new RendererCentrado());
        tabla.setDefaultRenderer(Integer.class, new RendererCentrado());
        tabla.setDefaultRenderer(Long.class, new RendererCentrado());
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel panel = crearContenedor("Comparacion de ejecucion");
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setBackground(FONDO_ELEGANTE);
        JButton btnGuardar = UiSupport.crearBoton("Guardar mejor combinacion", UiSupport.MORADO, Color.WHITE);
        JButton btnCerrar = UiSupport.crearBoton("Cerrar", ACENTO, Color.WHITE);
        btnGuardar.addActionListener(e -> guardarResultado());
        btnCerrar.addActionListener(e -> dispose());
        panel.add(btnGuardar);
        panel.add(btnCerrar);
        return panel;
    }

    private ResultadoMochila obtenerMejor() {
        return resultados.stream()
                .max((a, b) -> Integer.compare(a.getBeneficioTotal(), b.getBeneficioTotal()))
                .orElseThrow(() -> new IllegalStateException("No hay resultados para mostrar."));
    }

    private JPanel crearContenedor(String titulo) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(UiSupport.SUPERFICIE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(187, 211, 222)),
                new EmptyBorder(10, 12, 12, 12)));
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD, 16f));
        etiqueta.setForeground(new Color(35, 82, 99));
        panel.add(etiqueta, BorderLayout.NORTH);
        return panel;
    }

    private JPanel crearTarjetaMetrica(String titulo, String valor, Color fondo) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(fondo);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(179, 201, 211)),
                new EmptyBorder(8, 12, 8, 12)));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 3, 0);
        c.anchor = GridBagConstraints.CENTER;

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblValor.setForeground(new Color(22, 74, 86));
        panel.add(lblValor, c);

        c.gridy = 1;
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 12f));
        lblTitulo.setForeground(UiSupport.TEXTO);
        panel.add(lblTitulo, c);
        return panel;
    }

    private void guardarResultado() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Guardar mejor combinacion");
        selector.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos de texto (*.txt)", "txt"));
        selector.setSelectedFile(new File("resultado_mochila.txt"));
        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path ruta = asegurarExtensionTxt(selector.getSelectedFile()).toPath();
        try {
            archivoObjetos.guardarResultado(ruta, capacidad, objetos, resultados);
            JOptionPane.showMessageDialog(this,
                    "Resultado guardado en " + ruta.getFileName() + ".",
                    "Resultado guardado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el resultado:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private File asegurarExtensionTxt(File archivo) {
        return archivo.getName().toLowerCase().endsWith(".txt")
                ? archivo
                : new File(archivo.getParentFile(), archivo.getName() + ".txt");
    }

    private static final class RendererCentrado extends DefaultTableCellRenderer {
        private RendererCentrado() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
            Component componente = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                componente.setBackground(row % 2 == 0 ? Color.WHITE : new Color(247, 251, 253));
                componente.setForeground(UiSupport.TEXTO);
            }
            return componente;
        }
    }
}
