package Logica;

import Logica.RecursionLogic;
import Logica.MazeSolver;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    // Componentes principales
    private JTabbedPane tabbedPane;
    private JButton btnVolver;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    
    // --- PESTAÑA 2: RECURSIÓN VS ITERACIÓN ---
    private JComboBox<String> cbAlgoritmo;
    private JTextField txtInputN;
    private JTextArea txtAreaRecursivoLog;
    private JTextArea txtAreaIterativoLog;
    private JLabel lblResultadoRecursivo;
    private JLabel lblResultadoIterativo;
    private JLabel lblTiempoRecursivo;
    private JLabel lblTiempoIterativo;

    // --- PESTAÑA 3: RECURSIVIDAD INDIRECTA ---
    private JTextField txtInputParidad;
    private JTextArea txtAreaIndirectaLog;
    private JLabel lblResultadoParidad;

    // --- PESTAÑA 4: BACKTRACKING (LABERINTO) ---
    private JPanel pnlMazeBoard;
    private JPanel[][] cellPanels;
    private JTextArea txtAreaMazeLog;
    private JButton btnMazeStart;
    private JButton btnMazeNext;
    private JButton btnMazeAuto;
    private JButton btnMazeReset;
    private JLabel lblMazeStatus;
    
    // Datos del laberinto (0 = libre, 1 = pared)
    private final int[][] mazeGrid = {
        {0, 1, 0, 0, 0},
        {0, 0, 0, 1, 0},
        {1, 1, 0, 1, 0},
        {0, 0, 0, 0, 0},
        {0, 1, 1, 1, 0}
    };
    
    private MazeSolver solver;
    private List<MazeSolver.Step> mazeSteps;
    private List<String> mazeLogs;
    private int currentMazeStepIndex = -1;
    private Timer mazeTimer;

    public MainFrame() {
        super("PC3");
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        setupStyles();
        
        applyMediumFont(this);
        
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // ==================================================
        // LÓGICA DEL BOTÓN VOLVER
        // btnVolver.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         new (nombre del package del menu principal).(nombre del la clase del menu principal)().setVisible(true);
        //         dispose();
        //     }
        // });
    }

    private void initComponents() {
        // --- HEADER PANEL ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(243, 244, 246));
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(229, 231, 235)),
            new EmptyBorder(12, 20, 12, 20)
        ));

        JPanel pnlTitleText = new JPanel(new GridLayout(2, 1, 2, 2));
        pnlTitleText.setOpaque(false);
        titleLabel = new JLabel("EJERCICIO 3");
        subtitleLabel = new JLabel("Lógica Recursiva, Pila de Llamadas, Recursividad Indirecta y Backtracking");
        pnlTitleText.add(titleLabel);
        pnlTitleText.add(subtitleLabel);

        // BOTON PARA VOLVER AL MENU PRINCIPAL
        btnVolver = new JButton("VOLVER");
        btnVolver.setFocusPainted(false);
        btnVolver.setBackground(new Color(239, 68, 68));
        btnVolver.setForeground(Color.BLACK);
        
        JPanel pnlVolverWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        pnlVolverWrapper.setOpaque(false);
        pnlVolverWrapper.add(btnVolver);

        pnlHeader.add(pnlTitleText, BorderLayout.CENTER);
        pnlHeader.add(pnlVolverWrapper, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- TABBED PANE MAIN ---
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setOpaque(true);

        initTabRecursionVsIteration();
        initTabIndirectRecursion();
        initTabBacktracking();

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void setupStyles() {
        getContentPane().setBackground(new Color(249, 250, 251));
    }

    private void applyMediumFont(Component c) {
        if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) {
                applyMediumFont(child);
            }
        }
        
        Font currentFont = c.getFont();
        if (currentFont != null) {
            int style = currentFont.isBold() ? Font.BOLD : (currentFont.isItalic() ? Font.ITALIC : Font.PLAIN);
            c.setFont(new Font("Segoe UI", style, 14));
        } else {
            c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
    }

    // PESTAÑA 2: RECURSIÓN VS ITERACIÓN
    private void initTabRecursionVsIteration() {
        JPanel pnlMain = new JPanel(new BorderLayout(15, 15));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlControls.setBackground(new Color(249, 250, 251));
        pnlControls.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true));

        pnlControls.add(new JLabel("Algoritmo:"));
        cbAlgoritmo = new JComboBox<>(new String[]{"Factorial", "Fibonacci"});
        pnlControls.add(cbAlgoritmo);

        pnlControls.add(new JLabel("Valor N:"));
        txtInputN = new JTextField("5", 6);
        pnlControls.add(txtInputN);

        JButton btnCalcular = new JButton("Comparar Ejecución");
        btnCalcular.setBackground(new Color(37, 99, 235));
        btnCalcular.setForeground(Color.BLACK);
        btnCalcular.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCalcular.setFocusPainted(false);
        btnCalcular.setOpaque(true);
        btnCalcular.setContentAreaFilled(true);
        btnCalcular.setBorderPainted(true);
        btnCalcular.setRolloverEnabled(false);
        pnlControls.add(btnCalcular);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFocusPainted(false);
        pnlControls.add(btnLimpiar);

        pnlMain.add(pnlControls, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(15, 15));
        pnlCenter.setOpaque(false);

        JPanel pnlLogsGrid = new JPanel(new GridLayout(1, 2, 15, 15));
        pnlLogsGrid.setOpaque(false);

        JPanel pnlRecursivo = new JPanel(new BorderLayout(5, 5));
        pnlRecursivo.setOpaque(false);
        pnlRecursivo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            "1. PROCESO RECURSIVO (Pila de Llamadas)",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), new Color(37, 99, 235)
        ));
        txtAreaRecursivoLog = new JTextArea();
        txtAreaRecursivoLog.setEditable(false);
        txtAreaRecursivoLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        pnlRecursivo.add(new JScrollPane(txtAreaRecursivoLog), BorderLayout.CENTER);

        JPanel pnlIterativo = new JPanel(new BorderLayout(5, 5));
        pnlIterativo.setOpaque(false);
        pnlIterativo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            "2. PROCESO ITERATIVO (Bucle / Loops)",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), new Color(107, 114, 128)
        ));
        txtAreaIterativoLog = new JTextArea();
        txtAreaIterativoLog.setEditable(false);
        txtAreaIterativoLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        pnlIterativo.add(new JScrollPane(txtAreaIterativoLog), BorderLayout.CENTER);

        pnlLogsGrid.add(pnlRecursivo);
        pnlLogsGrid.add(pnlIterativo);
        pnlCenter.add(pnlLogsGrid, BorderLayout.CENTER);

        JPanel pnlResultados = new JPanel(new GridLayout(2, 2, 10, 8));
        pnlResultados.setBackground(new Color(243, 244, 246));
        pnlResultados.setBorder(new EmptyBorder(12, 15, 12, 15));
        
        lblResultadoRecursivo = new JLabel("Resultado Recursivo: -");
        lblResultadoIterativo = new JLabel("Resultado Iterativo: -");
        lblTiempoRecursivo = new JLabel("Tiempo Recursivo: -");
        lblTiempoIterativo = new JLabel("Tiempo Iterativo: -");
        
        pnlResultados.add(lblResultadoRecursivo);
        pnlResultados.add(lblResultadoIterativo);
        pnlResultados.add(lblTiempoRecursivo);
        pnlResultados.add(lblTiempoIterativo);
        
        pnlCenter.add(pnlResultados, BorderLayout.SOUTH);
        pnlMain.add(pnlCenter, BorderLayout.CENTER);

        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compararRecursionVsIteracion();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtAreaRecursivoLog.setText("");
                txtAreaIterativoLog.setText("");
                lblResultadoRecursivo.setText("Resultado Recursivo: -");
                lblResultadoIterativo.setText("Resultado Iterativo: -");
                lblTiempoRecursivo.setText("Tiempo Recursivo: -");
                lblTiempoIterativo.setText("Tiempo Iterativo: -");
            }
        });

        tabbedPane.addTab("Recursión vs Iteración", pnlMain);
    }

    private void compararRecursionVsIteracion() {
        String inputStr = txtInputN.getText().trim();
        int n;
        try {
            n = Integer.parseInt(inputStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un número entero válido.", "Entrada Inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String algoritmo = (String) cbAlgoritmo.getSelectedItem();
        List<String> recursiveLogs = new ArrayList<>();
        List<String> iterativeLogs = new ArrayList<>();
        
        long resultRecursivo = 0;
        long resultIterativo = 0;
        long tStart, tEnd;
        long timeRecursivoNs = 0;
        long timeIterativoNs = 0;

        if ("Factorial".equals(algoritmo)) {
            if (n > 20) {
                JOptionPane.showMessageDialog(this, "Para Factorial, el valor máximo sugerido es 20 (límite de tipo long).", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            tStart = System.nanoTime();
            resultRecursivo = RecursionLogic.factorialRecursivo(n, recursiveLogs, 0);
            tEnd = System.nanoTime();
            timeRecursivoNs = tEnd - tStart;
            
            tStart = System.nanoTime();
            resultIterativo = RecursionLogic.factorialIterativo(n, iterativeLogs);
            tEnd = System.nanoTime();
            timeIterativoNs = tEnd - tStart;

        } else if ("Fibonacci".equals(algoritmo)) {
            if (n > 25) {
                JOptionPane.showMessageDialog(this, "Para Fibonacci recursivo, el valor máximo sugerido es 25 para evitar retrasos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            tStart = System.nanoTime();
            resultRecursivo = RecursionLogic.fibonacciRecursivo(n, recursiveLogs, 0);
            tEnd = System.nanoTime();
            timeRecursivoNs = tEnd - tStart;
            
            tStart = System.nanoTime();
            resultIterativo = RecursionLogic.fibonacciIterativo(n, iterativeLogs);
            tEnd = System.nanoTime();
            timeIterativoNs = tEnd - tStart;
        }

        txtAreaRecursivoLog.setText(String.join("\n", recursiveLogs));
        txtAreaIterativoLog.setText(String.join("\n", iterativeLogs));

        lblResultadoRecursivo.setText("Resultado Recursivo: " + resultRecursivo);
        lblResultadoIterativo.setText("Resultado Iterativo: " + resultIterativo);
        
        lblTiempoRecursivo.setText(String.format("Tiempo Recursivo: %d ns (%.4f ms)", timeRecursivoNs, timeRecursivoNs / 1_000_000.0));
        lblTiempoIterativo.setText(String.format("Tiempo Iterativo: %d ns (%.4f ms)", timeIterativoNs, timeIterativoNs / 1_000_000.0));
    }

    // PESTAÑA 3: RECURSIVIDAD INDIRECTA
    private void initTabIndirectRecursion() {
        JPanel pnlMain = new JPanel(new BorderLayout(15, 15));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlControls.setBackground(new Color(249, 250, 251));
        pnlControls.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true));

        pnlControls.add(new JLabel("Número a verificar (Par/Impar):"));
        txtInputParidad = new JTextField("6", 8);
        pnlControls.add(txtInputParidad);

        JButton btnEvaluar = new JButton("Verificar con Recursividad Indirecta");
        btnEvaluar.setBackground(new Color(37, 99, 235));
        btnEvaluar.setForeground(Color.BLACK);
        btnEvaluar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEvaluar.setFocusPainted(false);
        btnEvaluar.setOpaque(true);
        btnEvaluar.setContentAreaFilled(true);
        btnEvaluar.setBorderPainted(true);
        btnEvaluar.setRolloverEnabled(false);
        btnEvaluar.setPreferredSize(new Dimension(310, 30));
        pnlControls.add(btnEvaluar);

        pnlMain.add(pnlControls, BorderLayout.NORTH);

        JPanel pnlLogPanel = new JPanel(new BorderLayout(5, 5));
        pnlLogPanel.setOpaque(false);
        pnlLogPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            "TRAZA DE RECURSIVIDAD INDIRECTA (Mutua llamada entre esPar y esImpar)",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), new Color(37, 99, 235)
        ));

        txtAreaIndirectaLog = new JTextArea();
        txtAreaIndirectaLog.setEditable(false);
        txtAreaIndirectaLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        pnlLogPanel.add(new JScrollPane(txtAreaIndirectaLog), BorderLayout.CENTER);

        lblResultadoParidad = new JLabel("Resultado: Esperando entrada...", JLabel.CENTER);
        lblResultadoParidad.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblResultadoParidad.setBorder(new EmptyBorder(10, 10, 10, 10));
        lblResultadoParidad.setBackground(new Color(243, 244, 246));
        lblResultadoParidad.setOpaque(true);
        pnlLogPanel.add(lblResultadoParidad, BorderLayout.SOUTH);

        pnlMain.add(pnlLogPanel, BorderLayout.CENTER);

        btnEvaluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                evaluarRecursividadIndirecta();
            }
        });

        tabbedPane.addTab("Recursividad Indirecta", pnlMain);
    }

    private void evaluarRecursividadIndirecta() {
        String inputStr = txtInputParidad.getText().trim();
        int n;
        try {
            n = Integer.parseInt(inputStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un número entero válido.", "Entrada Inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Math.abs(n) > 50) {
            JOptionPane.showMessageDialog(this, "Para evitar desbordamiento de pila, el valor máximo sugerido es 50.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> steps = new ArrayList<>();
        steps.add("Iniciando análisis recursivo indirecto para determinar paridad de N = " + n);
        boolean esNPar = RecursionLogic.esPar(n, steps, 0);
        
        txtAreaIndirectaLog.setText(String.join("\n", steps));
        lblResultadoParidad.setText("Resultado: El número " + n + " es " + (esNPar ? "PAR (true)" : "IMPAR (false)"));
    }

    // PESTAÑA 4: BACKTRACKING (LABERINTO)
    private void initTabBacktracking() {
        JPanel pnlMain = new JPanel(new BorderLayout(15, 15));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlLeft = new JPanel(new BorderLayout(15, 15));
        pnlLeft.setOpaque(false);
        pnlLeft.setPreferredSize(new Dimension(360, 0));

        JTextArea txtExplicacionMaze = new JTextArea(
            "Backtracking en Laberinto 5x5:\n\n" +
            "Se busca una ruta desde INICIO (0,0) hasta META (4,4).\n" +
            "• Gris Oscuro: Paredes transitables.\n" +
            "• Blanco: Caminos libres.\n" +
            "• Amarillo: Explorando actualmente.\n" +
            "• Rojo: Camino sin salida (Backtrack / Desmarcado).\n" +
            "• Verde: Celda en ruta de solución final.\n\n" +
            "Usa 'Paso Siguiente' para ver el flujo o 'Simulación Automática'."
        );
        txtExplicacionMaze.setEditable(false);
        txtExplicacionMaze.setLineWrap(true);
        txtExplicacionMaze.setWrapStyleWord(true);
        txtExplicacionMaze.setOpaque(false);
        txtExplicacionMaze.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtExplicacionMaze.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        pnlLeft.add(txtExplicacionMaze, BorderLayout.NORTH);

        pnlMazeBoard = new JPanel(new GridLayout(5, 5, 5, 5));
        pnlMazeBoard.setBackground(new Color(229, 231, 235));
        pnlMazeBoard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 2),
            new EmptyBorder(5, 5, 5, 5)
        ));

        cellPanels = new JPanel[5][5];
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                JPanel cell = new JPanel(new GridBagLayout());
                cell.setPreferredSize(new Dimension(50, 50));
                
                JLabel lbl = new JLabel();
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
                if (r == 0 && c == 0) {
                    lbl.setText("INICIO");
                    lbl.setForeground(Color.BLUE);
                } else if (r == 4 && c == 4) {
                    lbl.setText("META");
                    lbl.setForeground(new Color(22, 101, 52));
                }
                cell.add(lbl);
                
                cellPanels[r][c] = cell;
                pnlMazeBoard.add(cell);
            }
        }
        resetMazeBoardVisuals();

        pnlLeft.add(pnlMazeBoard, BorderLayout.CENTER);

        JPanel pnlMazeButtons = new JPanel(new GridLayout(3, 2, 8, 8));
        pnlMazeButtons.setOpaque(false);

        btnMazeStart = new JButton("Iniciar Búsqueda");
        btnMazeStart.setBackground(new Color(37, 99, 235));
        btnMazeStart.setForeground(Color.BLACK);
        btnMazeStart.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMazeStart.setFocusPainted(false);
        btnMazeStart.setOpaque(true);
        btnMazeStart.setContentAreaFilled(true);
        btnMazeStart.setBorderPainted(true);
        btnMazeStart.setRolloverEnabled(false);

        btnMazeNext = new JButton("Paso Siguiente");
        btnMazeNext.setFocusPainted(false);

        btnMazeAuto = new JButton("Simulación Auto");
        btnMazeAuto.setFocusPainted(false);

        btnMazeReset = new JButton("Restablecer");
        btnMazeReset.setFocusPainted(false);

        lblMazeStatus = new JLabel("Estado: Esperando inicio", JLabel.CENTER);
        lblMazeStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMazeStatus.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));

        pnlMazeButtons.add(btnMazeStart);
        pnlMazeButtons.add(btnMazeReset);
        pnlMazeButtons.add(btnMazeNext);
        pnlMazeButtons.add(btnMazeAuto);
        
        JPanel pnlLeftBottom = new JPanel(new BorderLayout(5, 5));
        pnlLeftBottom.setOpaque(false);
        pnlLeftBottom.add(pnlMazeButtons, BorderLayout.CENTER);
        pnlLeftBottom.add(lblMazeStatus, BorderLayout.SOUTH);
        
        pnlLeft.add(pnlLeftBottom, BorderLayout.SOUTH);
        pnlMain.add(pnlLeft, BorderLayout.WEST);

        JPanel pnlRightLog = new JPanel(new BorderLayout(5, 5));
        pnlRightLog.setOpaque(false);
        pnlRightLog.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            "REGISTRO DEL PROCESO DE BACKTRACKING (Paso a Paso)",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), new Color(37, 99, 235)
        ));

        txtAreaMazeLog = new JTextArea();
        txtAreaMazeLog.setEditable(false);
        txtAreaMazeLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        pnlRightLog.add(new JScrollPane(txtAreaMazeLog), BorderLayout.CENTER);

        pnlMain.add(pnlRightLog, BorderLayout.CENTER);

        btnMazeStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepararResolucionLaberinto();
            }
        });

        btnMazeNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                avanzarPasoLaberinto();
            }
        });

        btnMazeAuto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSimulacionAutomatica();
            }
        });

        btnMazeReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restablecerLaberintoCompleto();
            }
        });

        tabbedPane.addTab("Visualizar Backtracking", pnlMain);
    }

    private void resetMazeBoardVisuals() {
        if (mazeTimer != null && mazeTimer.isRunning()) {
            mazeTimer.stop();
        }
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                JPanel cell = cellPanels[r][c];
                if (mazeGrid[r][c] == 1) {
                    cell.setBackground(new Color(75, 85, 99));
                } else {
                    cell.setBackground(Color.WHITE);
                }
                cell.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
            }
        }
    }

    private void prepararResolucionLaberinto() {
        restablecerLaberintoCompleto();
        
        solver = new MazeSolver(mazeGrid);
        solver.solve();
        mazeSteps = solver.getDetailedSteps();
        mazeLogs = solver.getTextLogs();
        
        currentMazeStepIndex = 0;
        
        btnMazeStart.setEnabled(false);
        btnMazeNext.setEnabled(true);
        btnMazeAuto.setEnabled(true);
        btnMazeReset.setEnabled(true);
        
        lblMazeStatus.setText("Estado: Ejecución iniciada (" + mazeSteps.size() + " pasos)");
        txtAreaMazeLog.setText("Algoritmo inicializado. Haz clic en 'Paso Siguiente' o 'Simulación Auto' para comenzar la animación.\n\n");
        
        avanzarPasoLaberinto();
    }

    private void avanzarPasoLaberinto() {
        if (mazeSteps == null || currentMazeStepIndex >= mazeSteps.size()) {
            finalizarLaberinto();
            return;
        }

        MazeSolver.Step step = mazeSteps.get(currentMazeStepIndex);
        txtAreaMazeLog.append(step.message + "\n");
        txtAreaMazeLog.setCaretPosition(txtAreaMazeLog.getDocument().getLength());

        int r = step.r;
        int c = step.c;
        JPanel cell = cellPanels[r][c];

        switch (step.status) {
            case "VISIT":
                cell.setBackground(new Color(254, 240, 138));
                break;
            case "BACKTRACK":
                cell.setBackground(new Color(254, 202, 202));
                break;
            case "WALL":
                cell.setBackground(new Color(75, 85, 99));
                break;
            case "GOAL":
                cell.setBackground(new Color(187, 247, 208));
                break;
        }

        lblMazeStatus.setText("Paso: " + (currentMazeStepIndex + 1) + " / " + mazeSteps.size() + " (" + step.status + ")");
        currentMazeStepIndex++;
        
        if (currentMazeStepIndex >= mazeSteps.size()) {
            finalizarLaberinto();
        }
    }

    private void iniciarSimulacionAutomatica() {
        btnMazeNext.setEnabled(false);
        btnMazeAuto.setEnabled(false);
        
        mazeTimer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentMazeStepIndex < mazeSteps.size()) {
                    avanzarPasoLaberinto();
                } else {
                    mazeTimer.stop();
                    finalizarLaberinto();
                }
            }
        });
        mazeTimer.start();
    }

    private void finalizarLaberinto() {
        if (mazeTimer != null && mazeTimer.isRunning()) {
            mazeTimer.stop();
        }
        btnMazeNext.setEnabled(false);
        btnMazeAuto.setEnabled(false);
        
        if (solver != null && solver.getSolutionPath() != null) {
            txtAreaMazeLog.append("\n=== RUTA DE SOLUCIÓN ENCONTRADA ===\n");
            for (int[] point : solver.getSolutionPath()) {
                int r = point[0];
                int c = point[1];
                cellPanels[r][c].setBackground(new Color(187, 247, 208));
                txtAreaMazeLog.append("Celda (" + r + "," + c + ")\n");
            }
            lblMazeStatus.setText("Estado: ¡Laberinto Resuelto!");
        } else {
            lblMazeStatus.setText("Estado: Sin solución");
        }
        txtAreaMazeLog.setCaretPosition(txtAreaMazeLog.getDocument().getLength());
    }

    private void restablecerLaberintoCompleto() {
        if (mazeTimer != null && mazeTimer.isRunning()) {
            mazeTimer.stop();
        }
        
        currentMazeStepIndex = -1;
        mazeSteps = null;
        mazeLogs = null;
        solver = null;
        
        resetMazeBoardVisuals();
        
        txtAreaMazeLog.setText("");
        lblMazeStatus.setText("Estado: Esperando inicio");
        
        btnMazeStart.setEnabled(true);
        btnMazeNext.setEnabled(false);
        btnMazeAuto.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}