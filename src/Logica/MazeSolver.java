package Logica;

import java.util.ArrayList;
import java.util.List;

public class MazeSolver {

    public static class Step {
        public int r;
        public int c;
        public String status;
        public String message;

        public Step(int r, int c, String status, String message) {
            this.r = r;
            this.c = c;
            this.status = status;
            this.message = message;
        }
    }

    private final int[][] grid;
    private final int rows;
    private final int cols;
    private final boolean[][] visited;
    
    private final List<Step> detailedSteps;
    private final List<String> textLogs;
    private final List<int[]> solutionPath;

    public MazeSolver(int[][] customGrid) {
        this.grid = customGrid;
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.visited = new boolean[rows][cols];
        this.detailedSteps = new ArrayList<>();
        this.textLogs = new ArrayList<>();
        this.solutionPath = new ArrayList<>();
    }

    public List<Step> getDetailedSteps() {
        return detailedSteps;
    }

    public List<String> getTextLogs() {
        return textLogs;
    }

    public List<int[]> getSolutionPath() {
        return solutionPath;
    }

    public boolean solve() {
        detailedSteps.clear();
        textLogs.clear();
        solutionPath.clear();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                visited[r][c] = false;
            }
        }
        
        textLogs.add("Iniciando resolución del laberinto por backtracking...");
        boolean success = solveRecursive(0, 0, 0);
        if (success) {
            textLogs.add("¡Laberinto resuelto con éxito! Generando ruta de solución...");
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (visited[r][c]) {
                        solutionPath.add(new int[]{r, c});
                    }
                }
            }
        } else {
            textLogs.add("No se encontró solución para este laberinto.");
        }
        return success;
    }

    private boolean solveRecursive(int r, int c, int depth) {
        String indent = "  ".repeat(depth);
        
        // 1. Validar límites de la cuadrícula
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            textLogs.add(indent + "Celda (" + r + "," + c + ") fuera de límites. Retrocediendo.");
            return false;
        }
        
        // 2. Validar si es una pared
        if (grid[r][c] == 1) {
            detailedSteps.add(new Step(r, c, "WALL", "Pared en (" + r + "," + c + ")"));
            textLogs.add(indent + "Celda (" + r + "," + c + ") es una pared. Retrocediendo.");
            return false;
        }
        
        // 3. Validar si ya fue visitada
        if (visited[r][c]) {
            detailedSteps.add(new Step(r, c, "VISITED_BEFORE", "Ya visitado: (" + r + "," + c + ")"));
            textLogs.add(indent + "Celda (" + r + "," + c + ") ya visitada. Retrocediendo.");
            return false;
        }
        
        // Marcar como visitado
        visited[r][c] = true;
        detailedSteps.add(new Step(r, c, "VISIT", "Visitando: (" + r + "," + c + ")"));
        textLogs.add(indent + "Visitando celda: (" + r + "," + c + ")");

        // 4. Caso base: meta alcanzada (esquina inferior derecha)
        if (r == rows - 1 && c == cols - 1) {
            detailedSteps.add(new Step(r, c, "GOAL", "¡Meta alcanzada! (" + r + "," + c + ")"));
            textLogs.add(indent + "¡META ALCANZADA en (" + r + "," + c + ")!");
            return true;
        }

        // 5. Intentar movimientos (Abajo, Derecha, Arriba, Izquierda)
        int[] dRow = {1, 0, -1, 0};
        int[] dCol = {0, 1, 0, -1};
        String[] directions = {"Abajo", "Derecha", "Arriba", "Izquierda"};

        for (int i = 0; i < 4; i++) {
            int nextR = r + dRow[i];
            int nextC = c + dCol[i];
            textLogs.add(indent + "  Intentando mover hacia " + directions[i] + " a (" + nextR + "," + nextC + ")...");
            
            if (solveRecursive(nextR, nextC, depth + 1)) {
                return true;
            }
        }

        // 6. Si ningún movimiento funciona, hacemos Backtracking
        visited[r][c] = false; // Desmarcar
        detailedSteps.add(new Step(r, c, "BACKTRACK", "Retrocediendo de: (" + r + "," + c + ")"));
        textLogs.add(indent + "← Backtracking: Desmarcando celda (" + r + "," + c + ") - callejón sin salida.");
        return false;
    }
}