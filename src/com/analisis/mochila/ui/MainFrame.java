package com.analisis.mochila.ui;

import com.analisis.mochila.algoritmo.ModoAlgoritmo;
import com.analisis.mochila.io.ArchivoObjetos;
import com.analisis.mochila.io.DatosArchivo;
import com.analisis.mochila.model.ObjetoMochila;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public final class MainFrame extends JFrame {
    private final ObjetoTableModel modeloObjetos = new ObjetoTableModel();
    private final ArchivoObjetos archivoObjetos = new ArchivoObjetos();

    private JTable tablaObjetos;
    private JTextField txtNombre;
    private JSpinner spnPeso;
    private JSpinner spnBeneficio;
    private JSpinner spnCapacidad;
    private JComboBox<ModoAlgoritmo> cmbAlgoritmo;
    private JLabel lblEstado;
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private int filaEnEdicion = -1;

    public MainFrame() {
        super("Preparacion - Mochila 0/1");
        construirInterfaz();
        configurarVentana();
        cargarEjemploInicial();
    }

    public static void aplicarEstiloGlobal() {
        UiSupport.aplicarEstiloGlobal();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(980, 650));
        setSize(1120, 760);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salir();
            }
        });
    }

    private void construirInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBackground(UiSupport.FONDO);
        raiz.setBorder(new EmptyBorder(12, 14, 12, 14));
        setContentPane(raiz);

        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        raiz.add(crearCentro(), BorderLayout.CENTER);
        raiz.add(crearBarraInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout(12, 4));
        panel.setBackground(UiSupport.AZUL_CLARO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(156, 201, 228)),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel titulo = new JLabel("PREPARACION DEL PROBLEMA DE LA MOCHILA 0/1");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(20, 77, 115));
        JLabel subtitulo = new JLabel(
                "Cargue, cree o edite objetos antes de ejecutar el algoritmo.");
        subtitulo.setForeground(UiSupport.TEXTO);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 3));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);
        panel.add(textos, BorderLayout.WEST);

        JPanel archivos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        archivos.setOpaque(false);
        JButton btnCargar = UiSupport.crearBoton("Cargar lista", UiSupport.AZUL, Color.WHITE);
        JButton btnGuardar = UiSupport.crearBoton("Guardar lista", UiSupport.VERDE, Color.WHITE);
        JButton btnNueva = UiSupport.crearBoton("Nueva lista", UiSupport.NARANJA, Color.WHITE);
        btnCargar.addActionListener(e -> cargarLista());
        btnGuardar.addActionListener(e -> guardarLista());
        btnNueva.addActionListener(e -> nuevaLista());
        archivos.add(btnCargar);
        archivos.add(btnGuardar);
        archivos.add(btnNueva);
        panel.add(archivos, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearCentro() {
        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setBackground(UiSupport.FONDO);
        centro.add(crearPanelFormulario(), BorderLayout.WEST);
        centro.add(crearPanelTabla(), BorderLayout.CENTER);
        centro.add(crearPanelConfiguracion(), BorderLayout.SOUTH);
        return centro;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UiSupport.SUPERFICIE);
        panel.setPreferredSize(new Dimension(340, 260));
        panel.setBorder(UiSupport.crearBordeTitulado("Crear o editar objeto"));
        GridBagConstraints c = restricciones();

        txtNombre = new JTextField(18);
        spnPeso = new JSpinner(new SpinnerNumberModel(1, 1, 1_000_000, 1));
        spnBeneficio = new JSpinner(new SpinnerNumberModel(1, 0, 1_000_000, 1));

        agregarCampo(panel, c, 0, "Nombre:", txtNombre);
        agregarCampo(panel, c, 1, "Peso:", spnPeso);
        agregarCampo(panel, c, 2, "Beneficio:", spnBeneficio);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        botones.setOpaque(false);
        btnAgregar = UiSupport.crearBoton("Agregar", UiSupport.VERDE, Color.WHITE);
        btnActualizar = UiSupport.crearBoton("Actualizar", UiSupport.AZUL, Color.WHITE);
        btnEliminar = UiSupport.crearBoton("Eliminar", UiSupport.ROJO, Color.WHITE);
        JButton btnCancelar = UiSupport.crearBoton("Cancelar edicion", UiSupport.GRIS_CLARO, UiSupport.TEXTO);
        btnAgregar.addActionListener(e -> agregarObjeto());
        btnActualizar.addActionListener(e -> actualizarObjeto());
        btnEliminar.addActionListener(e -> eliminarObjeto());
        btnCancelar.addActionListener(e -> limpiarFormulario());
        botones.add(btnAgregar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnCancelar);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(botones, c);
        actualizarEstadoEdicion();
        return panel;
    }

    private JScrollPane crearPanelTabla() {
        tablaObjetos = new JTable(modeloObjetos);
        tablaObjetos.setRowHeight(31);
        tablaObjetos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaObjetos.setGridColor(new Color(220, 228, 235));
        tablaObjetos.setSelectionBackground(UiSupport.AZUL_CLARO);
        tablaObjetos.setSelectionForeground(UiSupport.TEXTO);
        tablaObjetos.getColumnModel().getColumn(0).setPreferredWidth(45);
        tablaObjetos.getColumnModel().getColumn(1).setPreferredWidth(260);
        tablaObjetos.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaObjetos.getColumnModel().getColumn(3).setPreferredWidth(110);
        tablaObjetos.setDefaultRenderer(Integer.class, new RendererCentrado());
        tablaObjetos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaObjetos.getSelectedRow() >= 0) {
                cargarObjetoEnFormulario(tablaObjetos.getSelectedRow());
            }
        });

        JScrollPane scroll = new JScrollPane(tablaObjetos);
        scroll.setBorder(UiSupport.crearBordeTitulado("Tabla de objetos disponibles"));
        scroll.getViewport().setBackground(UiSupport.SUPERFICIE);
        return scroll;
    }

    private JPanel crearPanelConfiguracion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UiSupport.SUPERFICIE);
        panel.setBorder(UiSupport.crearBordeTitulado("Configuracion del algoritmo"));
        GridBagConstraints c = restricciones();

        spnCapacidad = new JSpinner(new SpinnerNumberModel(10, 1, 10_000_000, 1));
        cmbAlgoritmo = new JComboBox<>(ModoAlgoritmo.values());
        cmbAlgoritmo.setSelectedItem(ModoAlgoritmo.BACKTRACKING_PODA);

        agregarCampo(panel, c, 0, "Capacidad de la mochila:", spnCapacidad);
        agregarCampo(panel, c, 1, "Algoritmo a ejecutar:", cmbAlgoritmo);

        JLabel ayuda = new JLabel("Al ejecutar se abrira una ventana para ver el proceso paso a paso.");
        ayuda.setForeground(new Color(88, 101, 112));
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(ayuda, c);
        return panel;
    }

    private JPanel crearBarraInferior() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(UiSupport.FONDO);

        JButton btnEjecutar = UiSupport.crearBoton("Ejecutar", UiSupport.MORADO, Color.WHITE);
        JButton btnSalir = UiSupport.crearBoton("Salir", UiSupport.GRIS, Color.WHITE);
        btnEjecutar.addActionListener(e -> ejecutar());
        btnSalir.addActionListener(e -> salir());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.setOpaque(false);
        acciones.add(btnEjecutar);
        acciones.add(btnSalir);
        panel.add(acciones, BorderLayout.WEST);

        lblEstado = new JLabel("Listo para preparar los datos.");
        lblEstado.setFont(lblEstado.getFont().deriveFont(Font.BOLD));
        lblEstado.setForeground(UiSupport.AZUL);
        panel.add(lblEstado, BorderLayout.EAST);
        return panel;
    }

    private void ejecutar() {
        if (modeloObjetos.getRowCount() == 0) {
            advertir("Agregue o cargue al menos un objeto.");
            return;
        }
        if (modeloObjetos.getRowCount() > 24
                && !confirmar("La busqueda puede crecer como 2^n y hay mas de 24 objetos.\nDesea continuar?")) {
            return;
        }
        EjecucionFrame ventana = new EjecucionFrame(
                this,
                modeloObjetos.getObjetos(),
                ((Number) spnCapacidad.getValue()).intValue(),
                (ModoAlgoritmo) cmbAlgoritmo.getSelectedItem());
        ventana.setVisible(true);
        lblEstado.setText("Ventana de ejecucion abierta.");
    }

    private void agregarObjeto() {
        try {
            modeloObjetos.agregar(leerFormulario());
            limpiarFormulario();
            lblEstado.setText("Objeto agregado a la lista.");
        } catch (IllegalArgumentException ex) {
            advertir(ex.getMessage());
        }
    }

    private void actualizarObjeto() {
        if (filaEnEdicion < 0) {
            advertir("Seleccione primero un objeto de la tabla.");
            return;
        }
        try {
            modeloObjetos.actualizar(filaEnEdicion, leerFormulario());
            limpiarFormulario();
            lblEstado.setText("Objeto actualizado.");
        } catch (IllegalArgumentException ex) {
            advertir(ex.getMessage());
        }
    }

    private void eliminarObjeto() {
        if (filaEnEdicion < 0) {
            advertir("Seleccione primero un objeto de la tabla.");
            return;
        }
        modeloObjetos.eliminar(filaEnEdicion);
        limpiarFormulario();
        lblEstado.setText("Objeto eliminado.");
    }

    private ObjetoMochila leerFormulario() {
        String nombre = txtNombre.getText().trim();
        int peso = ((Number) spnPeso.getValue()).intValue();
        int beneficio = ((Number) spnBeneficio.getValue()).intValue();
        return new ObjetoMochila(nombre, peso, beneficio);
    }

    private void cargarObjetoEnFormulario(int fila) {
        filaEnEdicion = fila;
        ObjetoMochila objeto = modeloObjetos.getObjeto(fila);
        txtNombre.setText(objeto.getNombre());
        spnPeso.setValue(objeto.getPeso());
        spnBeneficio.setValue(objeto.getBeneficio());
        actualizarEstadoEdicion();
    }

    private void limpiarFormulario() {
        filaEnEdicion = -1;
        tablaObjetos.clearSelection();
        txtNombre.setText("");
        spnPeso.setValue(1);
        spnBeneficio.setValue(1);
        txtNombre.requestFocusInWindow();
        actualizarEstadoEdicion();
    }

    private void actualizarEstadoEdicion() {
        if (btnAgregar == null) {
            return;
        }
        btnAgregar.setEnabled(filaEnEdicion < 0);
        btnActualizar.setEnabled(filaEnEdicion >= 0);
        btnEliminar.setEnabled(filaEnEdicion >= 0);
    }

    private void cargarLista() {
        JFileChooser selector = crearSelector("Cargar lista de objetos");
        if (selector.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            DatosArchivo datos = archivoObjetos.cargar(selector.getSelectedFile().toPath());
            modeloObjetos.reemplazarTodos(datos.getObjetos());
            if (datos.getCapacidad() != null) {
                spnCapacidad.setValue(datos.getCapacidad());
            }
            limpiarFormulario();
            lblEstado.setText("Lista cargada: " + selector.getSelectedFile().getName());
        } catch (IOException ex) {
            mostrarError("No se pudo cargar la lista:\n" + ex.getMessage());
        }
    }

    private void guardarLista() {
        if (modeloObjetos.getRowCount() == 0) {
            advertir("No hay objetos para guardar.");
            return;
        }
        JFileChooser selector = crearSelector("Guardar lista de objetos");
        selector.setSelectedFile(new File("objetos_mochila.txt"));
        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path ruta = asegurarExtensionTxt(selector.getSelectedFile()).toPath();
        try {
            archivoObjetos.guardar(
                    ruta,
                    modeloObjetos.getObjetos(),
                    ((Number) spnCapacidad.getValue()).intValue());
            lblEstado.setText("Lista guardada en " + ruta.getFileName() + ".");
        } catch (IOException ex) {
            mostrarError("No se pudo guardar la lista:\n" + ex.getMessage());
        }
    }

    private void nuevaLista() {
        if (!confirmar("Se eliminaran los objetos actuales. Desea continuar?")) {
            return;
        }
        modeloObjetos.limpiar();
        limpiarFormulario();
        spnCapacidad.setValue(10);
        lblEstado.setText("Nueva lista preparada.");
    }

    private void salir() {
        if (confirmar("Desea cerrar el programa?")) {
            dispose();
        }
    }

    private void cargarEjemploInicial() {
        modeloObjetos.agregar(new ObjetoMochila("Laptop", 3, 4));
        modeloObjetos.agregar(new ObjetoMochila("Camara", 2, 3));
        modeloObjetos.agregar(new ObjetoMochila("Libro", 4, 5));
        modeloObjetos.agregar(new ObjetoMochila("Botella", 1, 1));
        spnCapacidad.setValue(5);
    }

    private JFileChooser crearSelector(String titulo) {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle(titulo);
        selector.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos de texto (*.txt)", "txt"));
        return selector;
    }

    private File asegurarExtensionTxt(File archivo) {
        return archivo.getName().toLowerCase().endsWith(".txt")
                ? archivo
                : new File(archivo.getParentFile(), archivo.getName() + ".txt");
    }

    private boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(
                this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
                == JOptionPane.YES_OPTION;
    }

    private void advertir(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Atencion", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private GridBagConstraints restricciones() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.anchor = GridBagConstraints.WEST;
        return c;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints c, int fila, String etiqueta, Component campo) {
        c.gridx = 0;
        c.gridy = fila;
        c.gridwidth = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campo, c);
    }

    private static final class RendererCentrado extends DefaultTableCellRenderer {
        private RendererCentrado() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
}
