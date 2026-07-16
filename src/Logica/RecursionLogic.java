package Logica;

import java.util.List;

public class RecursionLogic {

    // RECURSIÓN VS ITERACIÓN (FACTORIAL)

    /*Calcula factorial de forma recursiva registrando las llamadas en la pila*/
    public static long factorialRecursivo(int n, List<String> steps, int depth) {
        String indent = "  ".repeat(depth);
        steps.add(indent + "→ Entrando: factorial(" + n + ")");
        
        if (n < 0) {
            steps.add(indent + "Error: factorial no definido para números negativos.");
            return -1;
        }
        if (n <= 1) {
            steps.add(indent + "← Caso Base alcanzado: factorial(" + n + ") = 1");
            return 1;
        }
        
        long subResult = factorialRecursivo(n - 1, steps, depth + 1);
        long result = n * subResult;
        steps.add(indent + "← Retornando: " + n + " * factorial(" + (n - 1) + ") = " + result);
        return result;
    }

    /*Calcula factorial de forma iterativa registrando los cambios en las variables*/
    public static long factorialIterativo(int n, List<String> steps) {
        steps.add("Inicio de bucle iterativo para factorial(" + n + ")");
        if (n < 0) {
            steps.add("Error: factorial no definido para números negativos.");
            return -1;
        }
        long result = 1;
        steps.add("Estado inicial: resultado = 1");
        for (int i = 1; i <= n; i++) {
            long prev = result;
            result *= i;
            steps.add("Paso i = " + i + ": resultado = " + prev + " * " + i + " = " + result);
        }
        steps.add("Fin de bucle. Resultado final = " + result);
        return result;
    }

    // RECURSIÓN VS ITERACIÓN (FIBONACCI)

    /*Calcula Fibonacci de forma recursiva registrando el árbol de llamadas*/
    public static long fibonacciRecursivo(int n, List<String> steps, int depth) {
        String indent = "  ".repeat(depth);
        steps.add(indent + "→ Entrando: fibonacci(" + n + ")");
        
        if (n < 0) {
            steps.add(indent + "Error: fibonacci no definido para números negativos.");
            return -1;
        }
        if (n == 0) {
            steps.add(indent + "← Caso Base: fibonacci(0) = 0");
            return 0;
        }
        if (n == 1) {
            steps.add(indent + "← Caso Base: fibonacci(1) = 1");
            return 1;
        }
        
        long f1 = fibonacciRecursivo(n - 1, steps, depth + 1);
        long f2 = fibonacciRecursivo(n - 2, steps, depth + 1);
        long result = f1 + f2;
        steps.add(indent + "← Retornando: fibonacci(" + n + ") = " + f1 + " + " + f2 + " = " + result);
        return result;
    }

    /*Calcula Fibonacci de forma iterativa registrando los cambios de estado*/
    public static long fibonacciIterativo(int n, List<String> steps) {
        steps.add("Inicio de bucle iterativo para fibonacci(" + n + ")");
        if (n < 0) {
            steps.add("Error: fibonacci no definido para números negativos.");
            return -1;
        }
        if (n == 0) {
            steps.add("Caso base directo: N = 0. Resultado = 0");
            return 0;
        }
        if (n == 1) {
            steps.add("Caso base directo: N = 1. Resultado = 1");
            return 1;
        }
        
        long a = 0;
        long b = 1;
        steps.add("Valores iniciales: F(0) = 0, F(1) = 1");
        
        long result = 0;
        for (int i = 2; i <= n; i++) {
            result = a + b;
            steps.add("Paso i = " + i + ": F(" + i + ") = F(" + (i-2) + ") + F(" + (i-1) + ") = " + a + " + " + b + " = " + result);
            a = b;
            b = result;
        }
        steps.add("Fin de bucle. Resultado final = " + result);
        return result;
    }

    // RECURSIVIDAD INDIRECTA (PAR / IMPAR)

    /*Evalúa si un número es par de forma recursiva indirecta*/
    public static boolean esPar(int n, List<String> steps, int depth) {
        String indent = "  ".repeat(depth);
        steps.add(indent + "→ Entrando a esPar(" + n + ")");
        
        if (n < 0) {
            steps.add(indent + "Valor negativo recibido. Convirtiendo a positivo.");
            return esPar(-n, steps, depth);
        }
        if (n == 0) {
            steps.add(indent + "← Caso Base en esPar(0): es PAR (retorna true)");
            return true;
        }
        
        steps.add(indent + "  esPar(" + n + ") delega en esImpar(" + (n - 1) + ")");
        boolean result = esImpar(n - 1, steps, depth + 1);
        steps.add(indent + "← Retornando de esPar(" + n + ") con resultado: " + result);
        return result;
    }

    /*Evalúa si un número es impar de forma recursiva indirecta*/
    public static boolean esImpar(int n, List<String> steps, int depth) {
        String indent = "  ".repeat(depth);
        steps.add(indent + "→ Entrando a esImpar(" + n + ")");
        
        if (n < 0) {
            steps.add(indent + "Valor negativo recibido. Convirtiendo a positivo.");
            return esImpar(-n, steps, depth);
        }
        if (n == 0) {
            steps.add(indent + "← Caso Base en esImpar(0): NO es IMPAR (retorna false)");
            return false;
        }
        
        steps.add(indent + "  esImpar(" + n + ") delega en esPar(" + (n - 1) + ")");
        boolean result = esPar(n - 1, steps, depth + 1);
        steps.add(indent + "← Retornando de esImpar(" + n + ") con resultado: " + result);
        return result;
    }
}