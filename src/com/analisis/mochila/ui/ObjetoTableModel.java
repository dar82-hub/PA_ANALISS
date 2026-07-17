package com.analisis.mochila.ui;

import com.analisis.mochila.model.ObjetoMochila;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public final class ObjetoTableModel extends AbstractTableModel {
    private static final String[] COLUMNAS = {"Nro.", "Objeto", "Peso", "Beneficio"};
    private final List<ObjetoMochila> objetos = new ArrayList<>();

    @Override
    public int getRowCount() {
        return objetos.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int columna) {
        return COLUMNAS[columna];
    }

    @Override
    public Class<?> getColumnClass(int columna) {
        return columna == 1 ? String.class : Integer.class;
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        ObjetoMochila objeto = objetos.get(fila);
        switch (columna) {
            case 0:
                return fila + 1;
            case 1:
                return objeto.getNombre();
            case 2:
                return objeto.getPeso();
            case 3:
                return objeto.getBeneficio();
            default:
                return null;
        }
    }

    public void agregar(ObjetoMochila objeto) {
        int fila = objetos.size();
        objetos.add(objeto);
        fireTableRowsInserted(fila, fila);
    }

    public void actualizar(int fila, ObjetoMochila objeto) {
        objetos.set(fila, objeto);
        fireTableRowsUpdated(fila, fila);
    }

    public void eliminar(int fila) {
        objetos.remove(fila);
        fireTableRowsDeleted(fila, fila);
    }

    public void reemplazarTodos(List<ObjetoMochila> nuevos) {
        objetos.clear();
        objetos.addAll(nuevos);
        fireTableDataChanged();
    }

    public void limpiar() {
        objetos.clear();
        fireTableDataChanged();
    }

    public ObjetoMochila getObjeto(int fila) {
        return objetos.get(fila);
    }

    public List<ObjetoMochila> getObjetos() {
        return new ArrayList<>(objetos);
    }
}
