package com.analisis.mochila.ui;

import com.analisis.mochila.algoritmo.ControlEjecucion;
import com.analisis.mochila.algoritmo.EventoAlgoritmo;
import com.analisis.mochila.algoritmo.ModoAlgoritmo;
import com.analisis.mochila.algoritmo.SolucionadorMochila;
import com.analisis.mochila.algoritmo.TipoEvento;
import com.analisis.mochila.model.ObjetoMochila;
import com.analisis.mochila.model.ResultadoMochila;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public final class EjecucionFrame extends JFrame {
    private final JFrame padre;
    private final List<ObjetoMochila> objetos;
    private final int capacidad;
    private final ModoAlgoritmo modo;
    private final SolucionadorMochila solucionador = new SolucionadorMochila();
    private final List<EventoAlgoritmo> eventos = new ArrayList<>();

    private JLabel lblEstado;
    private JLabel lblPaso;
    private JLabel lblObjetoActual;
    private JLabel lblBeneficioActual;
    private JLabel lblExplicacion;
    private JProgressBar barraPeso;
    private JPanel pnlMochila;
    private JTextPane txtProceso;
    private JList<String> lstLeyenda;
    private JCheckBox chkAutomatico;
    private JSlider sldVelocidad;
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private JButton btnPausar;
    private JButton btnReiniciar;

    private Timer temporizador;
    private TareaGeneracion tarea;
    private ControlEjecucion controlGeneracion;
    private List<ResultadoMochila> resultados = Collections.emptyList();
    private int indicePaso = -1;
    private boolean resultadoMostrado;

    public EjecucionFrame(JFrame padre, List<ObjetoMochila> objetos, int capacidad, ModoAlgoritmo modo) {
        super("Ejecucion paso a paso - Mochila 0/1");
        this.padre = padre;
        this.objetos = new ArrayList<>(objetos);
        this.capacidad = capacidad;
        this.modo = modo;
        construirInterfaz();
        configurarVentana();
        iniciarGeneracion();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 700));
        setSize(1320, 820);
        setLocationRelativeTo(padre);
    }

    private void construirInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBackground(UiSupport.FONDO);
        raiz.setBorder(new EmptyBorder(10, 12, 10, 12));
        setContentPane(raiz);

        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        raiz.add(crearCentro(), BorderLayout.CENTER);
        raiz.add(crearControles(), BorderLayout.SOUTH);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout(10, 4));
        panel.setBackground(UiSupport.AZUL_CLARO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(156, 201, 228)),
                new EmptyBorder(10, 14, 10, 14)));

        JLabel titulo = new JLabel("EJECUCION DEL ALGORITMO");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(20, 77, 115));
        JLabel subtitulo = new JLabel("Modo: " + modo + "  |  Capacidad: " + capacidad);
        subtitulo.setForeground(UiSupport.TEXTO);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);
        panel.add(textos, BorderLayout.WEST);

        lblEstado = new JLabel("Preparando pasos del algoritmo...");
        lblEstado.setFont(lblEstado.getFont().deriveFont(Font.BOLD));
        lblEstado.setForeground(UiSupport.AZUL);
        panel.add(lblEstado, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearCentro() {
        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setBackground(UiSupport.FONDO);
        centro.add(crearPanelEstado(), BorderLayout.NORTH);
        centro.add(crearPanelProceso(), BorderLayout.CENTER);
        centro.add(crearPanelLeyenda(), BorderLayout.EAST);
        return centro;
    }

    private JPanel crearPanelEstado() {
        JPanel panel = new JPanel(new BorderLayout(9, 9));
        panel.setBackground(UiSupport.SUPERFICIE);
        panel.setBorder(UiSupport.crearBordeTitulado("Estado actual de la mochila"));

        JPanel indicadores = new JPanel(new GridLayout(1, 4, 8, 0));
        indicadores.setOpaque(false);
        lblPaso = crearIndicador("Paso 0 / 0", UiSupport.AZUL_CLARO);
        lblObjetoActual = crearIndicador("Objeto: -", UiSupport.GRIS_CLARO);
        lblBeneficioActual = crearIndicador("Beneficio: 0", UiSupport.MORADO_CLARO);
        barraPeso = new JProgressBar(0, capacidad);
        barraPeso.setValue(0);
        barraPeso.setStringPainted(true);
        barraPeso.setString("Peso actual: 0 / " + capacidad);
        barraPeso.setForeground(UiSupport.VERDE);
        barraPeso.setBackground(new Color(235, 241, 246));
        barraPeso.setBorder(BorderFactory.createLineBorder(UiSupport.BORDE));
        indicadores.add(lblPaso);
        indicadores.add(lblObjetoActual);
        indicadores.add(lblBeneficioActual);
        indicadores.add(barraPeso);
        panel.add(indicadores, BorderLayout.NORTH);

        pnlMochila = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        pnlMochila.setBackground(new Color(250, 252, 254));
        pnlMochila.setBorder(BorderFactory.createLineBorder(UiSupport.BORDE));
        pnlMochila.setPreferredSize(new Dimension(200, 68));
        mostrarMochila(Collections.emptyList());
        panel.add(pnlMochila, BorderLayout.CENTER);

        lblExplicacion = new JLabel("Se mostrara aqui la explicacion del paso actual.");
        lblExplicacion.setOpaque(true);
        lblExplicacion.setBackground(UiSupport.AZUL_CLARO);
        lblExplicacion.setForeground(UiSupport.TEXTO);
        lblExplicacion.setFont(lblExplicacion.getFont().deriveFont(Font.BOLD, 15f));
        lblExplicacion.setBorder(new EmptyBorder(8, 10, 8, 10));
        panel.add(lblExplicacion, BorderLayout.SOUTH);
        return panel;
    }

    private JScrollPane crearPanelProceso() {
        txtProceso = new JTextPane();
        txtProceso.setEditable(false);
        txtProceso.setBackground(UiSupport.SUPERFICIE);
        txtProceso.setForeground(UiSupport.TEXTO);
        txtProceso.setMargin(new Insets(9, 12, 9, 12));
        txtProceso.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JScrollPane scroll = new JScrollPane(txtProceso);
        scroll.setBorder(UiSupport.crearBordeTitulado("Proceso logico del algoritmo"));
        return scroll;
    }

    private JScrollPane crearPanelLeyenda() {
        lstLeyenda = new JList<>(new String[]{
            "[LLAMADA] Recursividad",
            "[INCLUIR] Toma el objeto",
            "[EXCLUIR] No toma el objeto",
            "[VOLVER] Backtracking",
            "[PODA] Rama descartada",
            "[MEJOR] Nueva seleccion optima",
            "[FIN] Resultado final"
        });
        lstLeyenda.setFixedCellHeight(30);
        lstLeyenda.setBackground(UiSupport.SUPERFICIE);
        lstLeyenda.setForeground(UiSupport.TEXTO);
        JScrollPane scroll = new JScrollPane(lstLeyenda);
        scroll.setPreferredSize(new Dimension(245, 120));
        scroll.setBorder(UiSupport.crearBordeTitulado("Leyenda para exponer"));
        return scroll;
    }

    private JPanel crearControles() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(UiSupport.FONDO);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        acciones.setOpaque(false);
        btnAnterior = UiSupport.crearBoton("Anterior", UiSupport.GRIS, Color.WHITE);
        btnSiguiente = UiSupport.crearBoton("Siguiente", UiSupport.AZUL, Color.WHITE);
        btnPausar = UiSupport.crearBoton("Pausar", UiSupport.NARANJA, Color.WHITE);
        btnReiniciar = UiSupport.crearBoton("Reiniciar", UiSupport.MORADO, Color.WHITE);
        JButton btnCerrar = UiSupport.crearBoton("Cerrar", UiSupport.GRIS_CLARO, UiSupport.TEXTO);
        btnAnterior.addActionListener(e -> irAnterior());
        btnSiguiente.addActionListener(e -> irSiguiente());
        btnPausar.addActionListener(e -> pausarContinuar());
        btnReiniciar.addActionListener(e -> reiniciar());
        btnCerrar.addActionListener(e -> dispose());
        acciones.add(btnAnterior);
        acciones.add(btnSiguiente);
        acciones.add(btnPausar);
        acciones.add(btnReiniciar);
        acciones.add(btnCerrar);
        panel.add(acciones, BorderLayout.WEST);

        JPanel velocidad = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        velocidad.setOpaque(false);
        chkAutomatico = new JCheckBox("Automatico", true);
        chkAutomatico.setBackground(UiSupport.FONDO);
        chkAutomatico.addActionListener(e -> actualizarAuto());
        sldVelocidad = new JSlider(0, 100, 55);
        sldVelocidad.setBackground(UiSupport.FONDO);
        sldVelocidad.setMajorTickSpacing(25);
        sldVelocidad.setPaintTicks(true);
        sldVelocidad.setToolTipText("0 = lento; 100 = resultado directo");
        sldVelocidad.addChangeListener(e -> actualizarDemora());
        velocidad.add(chkAutomatico);
        velocidad.add(new JLabel("Velocidad:"));
        velocidad.add(sldVelocidad);
        panel.add(velocidad, BorderLayout.EAST);

        establecerControles(false);
        return panel;
    }

    private void iniciarGeneracion() {
        tarea = new TareaGeneracion();
        tarea.execute();
    }

    private void iniciarReproduccion() {
        if (eventos.isEmpty()) {
            lblEstado.setText("No se generaron pasos.");
            return;
        }
        establecerControles(true);
        indicePaso = -1;
        txtProceso.setText("");
        irSiguiente();
        if (chkAutomatico.isSelected()) {
            if (esVelocidadMaxima()) {
                saltarAlResultado();
            } else {
                iniciarTimer();
            }
        }
    }

    private void iniciarTimer() {
        detenerTimer();
        if (esVelocidadMaxima()) {
            saltarAlResultado();
            return;
        }
        temporizador = new Timer(calcularDemora(), e -> {
            if (indicePaso < eventos.size() - 1) {
                irSiguiente();
            } else {
                detenerTimer();
                mostrarResultadoFinal();
            }
        });
        temporizador.start();
        btnPausar.setText("Pausar");
    }

    private void detenerTimer() {
        if (temporizador != null) {
            temporizador.stop();
            temporizador = null;
        }
    }

    private void actualizarDemora() {
        if (esVelocidadMaxima() && eventosGenerados()) {
            saltarAlResultado();
            return;
        }
        if (temporizador != null) {
            temporizador.setDelay(calcularDemora());
        }
    }

    private void actualizarAuto() {
        if (!chkAutomatico.isSelected()) {
            detenerTimer();
            btnPausar.setText("Continuar");
        } else if (eventosGenerados() && indicePaso < eventos.size() - 1) {
            if (esVelocidadMaxima()) {
                saltarAlResultado();
            } else {
                iniciarTimer();
            }
        }
    }

    private void pausarContinuar() {
        if (!eventosGenerados()) {
            return;
        }
        if (temporizador != null) {
            detenerTimer();
            chkAutomatico.setSelected(false);
            btnPausar.setText("Continuar");
            lblEstado.setText("Reproduccion pausada.");
        } else {
            chkAutomatico.setSelected(true);
            if (esVelocidadMaxima()) {
                saltarAlResultado();
            } else {
                iniciarTimer();
            }
            lblEstado.setText("Reproduccion automatica activa.");
        }
    }

    private void irSiguiente() {
        if (!eventosGenerados() || indicePaso >= eventos.size() - 1) {
            mostrarResultadoFinal();
            return;
        }
        indicePaso++;
        mostrarPasoActual(true);
    }

    private void irAnterior() {
        if (!eventosGenerados() || indicePaso <= 0) {
            return;
        }
        detenerTimer();
        chkAutomatico.setSelected(false);
        btnPausar.setText("Continuar");
        indicePaso--;
        reconstruirProcesoHasta(indicePaso);
        mostrarPasoActual(false);
    }

    private void reiniciar() {
        if (!eventosGenerados()) {
            return;
        }
        detenerTimer();
        resultadoMostrado = false;
        indicePaso = -1;
        txtProceso.setText("");
        irSiguiente();
        if (chkAutomatico.isSelected()) {
            iniciarTimer();
        }
    }

    private void mostrarPasoActual(boolean agregarAlLog) {
        EventoAlgoritmo evento = eventos.get(indicePaso);
        if (agregarAlLog) {
            agregarLinea(evento);
        }
        lblPaso.setText("Paso " + (indicePaso + 1) + " / " + eventos.size());
        lblObjetoActual.setText("Objeto: "
                + (evento.getObjeto() == null ? "-" : evento.getObjeto().getNombre()));
        lblObjetoActual.setBackground(UiSupport.fondoEvento(evento.getTipo()));
        lblBeneficioActual.setText("Beneficio: " + evento.getBeneficioActual());
        lblExplicacion.setText("<html>" + UiSupport.etiquetaEvento(evento.getTipo())
                + " " + evento.getMensaje() + "</html>");
        lblExplicacion.setBackground(UiSupport.fondoEvento(evento.getTipo()));
        actualizarBarraPeso(evento.getPesoActual());
        mostrarMochila(evento.getSeleccionActual());
        lblEstado.setText("Mostrando " + UiSupport.etiquetaEvento(evento.getTipo()));

        btnAnterior.setEnabled(indicePaso > 0);
        btnSiguiente.setEnabled(indicePaso < eventos.size() - 1);
        if (indicePaso == eventos.size() - 1) {
            mostrarResultadoFinal();
        }
    }

    private void saltarAlResultado() {
        if (!eventosGenerados()) {
            return;
        }
        detenerTimer();
        indicePaso = eventos.size() - 1;
        txtProceso.setText("");
        EventoAlgoritmo primero = eventos.get(0);
        EventoAlgoritmo ultimo = eventos.get(indicePaso);
        agregarLinea(primero);
        if (eventos.size() > 1) {
            agregarLinea(new EventoAlgoritmo(
                    TipoEvento.INICIO,
                    "Velocidad maxima: se omite la animacion y se muestra directamente el resultado.",
                    null, 0, 0, 0, Collections.emptyList()));
            agregarLinea(ultimo);
        }
        mostrarPasoActual(false);
        lblEstado.setText("Velocidad maxima: resultado mostrado directamente.");
    }

    private void reconstruirProcesoHasta(int indice) {
        txtProceso.setText("");
        for (int i = 0; i <= indice; i++) {
            agregarLinea(eventos.get(i));
        }
    }

    private void actualizarBarraPeso(int peso) {
        int valor = Math.max(0, Math.min(peso, capacidad));
        barraPeso.setValue(valor);
        barraPeso.setString("Peso actual: " + peso + " / " + capacidad);
        barraPeso.setForeground(peso > capacidad ? UiSupport.ROJO : UiSupport.VERDE);
    }

    private void mostrarMochila(List<ObjetoMochila> seleccion) {
        pnlMochila.removeAll();
        if (seleccion.isEmpty()) {
            JLabel vacia = new JLabel("Mochila vacia");
            vacia.setForeground(new Color(102, 113, 122));
            pnlMochila.add(vacia);
        } else {
            for (ObjetoMochila objeto : seleccion) {
                JLabel etiqueta = new JLabel(objeto.getNombre() + " (" + objeto.getPeso() + ")");
                etiqueta.setOpaque(true);
                etiqueta.setBackground(UiSupport.VERDE_CLARO);
                etiqueta.setForeground(new Color(25, 91, 56));
                etiqueta.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(99, 175, 132)),
                        new EmptyBorder(5, 8, 5, 8)));
                pnlMochila.add(etiqueta);
            }
        }
        pnlMochila.revalidate();
        pnlMochila.repaint();
    }

    private void agregarLinea(EventoAlgoritmo evento) {
        StyledDocument documento = txtProceso.getStyledDocument();
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, UiSupport.colorEvento(evento.getTipo()));
        StyleConstants.setBold(estilo, evento.getTipo() == TipoEvento.MEJOR_SOLUCION
                || evento.getTipo() == TipoEvento.PODA
                || evento.getTipo() == TipoEvento.INICIO
                || evento.getTipo() == TipoEvento.FIN);
        StyleConstants.setFontSize(estilo, 15);
        String sangria = repetir("  ", Math.min(evento.getProfundidad(), 9));
        String linea = sangria + UiSupport.etiquetaEvento(evento.getTipo())
                + " " + evento.getMensaje() + "\n";
        try {
            documento.insertString(documento.getLength(), linea, estilo);
            txtProceso.setCaretPosition(documento.getLength());
        } catch (BadLocationException ignored) {
            // El documento se actualiza en el hilo de Swing.
        }
    }

    private void mostrarResultadoFinal() {
        if (resultadoMostrado || resultados.isEmpty()) {
            return;
        }
        resultadoMostrado = true;
        detenerTimer();
        btnPausar.setText("Continuar");
        lblEstado.setText("Proceso finalizado. Mostrando solucion optima.");
        SwingUtilities.invokeLater(() -> new ResultadoFrame(
                this, objetos, capacidad, resultados).setVisible(true));
    }

    private boolean eventosGenerados() {
        return !eventos.isEmpty();
    }

    private boolean esVelocidadMaxima() {
        return sldVelocidad != null && sldVelocidad.getValue() >= sldVelocidad.getMaximum();
    }

    private int calcularDemora() {
        int velocidad = sldVelocidad.getValue();
        return 80 + ((100 - velocidad) * 11);
    }

    private void establecerControles(boolean activos) {
        btnAnterior.setEnabled(false);
        btnSiguiente.setEnabled(activos);
        btnPausar.setEnabled(activos);
        btnReiniciar.setEnabled(activos);
        chkAutomatico.setEnabled(activos);
        sldVelocidad.setEnabled(activos);
    }

    private JLabel crearIndicador(String texto, Color fondo) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setOpaque(true);
        etiqueta.setBackground(fondo);
        etiqueta.setForeground(UiSupport.TEXTO);
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD));
        etiqueta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiSupport.BORDE),
                new EmptyBorder(8, 8, 8, 8)));
        return etiqueta;
    }

    private String repetir(String texto, int veces) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            resultado.append(texto);
        }
        return resultado.toString();
    }

    private final class TareaGeneracion extends SwingWorker<List<ResultadoMochila>, EventoAlgoritmo> {
        @Override
        protected List<ResultadoMochila> doInBackground() {
            controlGeneracion = new ControlEjecucion(0);
            List<ResultadoMochila> salida = new ArrayList<>();
            if (modo == ModoAlgoritmo.COMPARAR) {
                salida.add(solucionador.resolver(
                        objetos,
                        capacidad,
                        ModoAlgoritmo.RECURSIVO_EXHAUSTIVO,
                        this::publicar,
                        controlGeneracion));
                publicar(new EventoAlgoritmo(
                        TipoEvento.INICIO,
                        "----- SEGUNDO ALGORITMO DE LA COMPARACION -----",
                        null, 0, 0, 0, Collections.emptyList()));
                salida.add(solucionador.resolver(
                        objetos,
                        capacidad,
                        ModoAlgoritmo.BACKTRACKING_PODA,
                        this::publicar,
                        controlGeneracion));
            } else {
                salida.add(solucionador.resolver(objetos, capacidad, modo, this::publicar, controlGeneracion));
            }
            return salida;
        }

        private void publicar(EventoAlgoritmo evento) {
            publish(evento);
        }

        @Override
        protected void process(List<EventoAlgoritmo> nuevos) {
            eventos.addAll(nuevos);
            lblEstado.setText("Pasos generados: " + eventos.size());
        }

        @Override
        protected void done() {
            try {
                resultados = get();
                lblEstado.setText("Pasos listos: " + eventos.size() + ". Iniciando reproduccion.");
                iniciarReproduccion();
            } catch (CancellationException ex) {
                lblEstado.setText("Generacion detenida.");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                JOptionPane.showMessageDialog(EjecucionFrame.this,
                        "La ejecucion fue interrumpida.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ExecutionException ex) {
                JOptionPane.showMessageDialog(EjecucionFrame.this,
                        "No se pudo resolver el problema:\n" + ex.getCause().getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                controlGeneracion = null;
            }
        }
    }
}
