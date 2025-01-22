package de.hhu.cs.stups.algvis.data.structures.table;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class DataTableModel implements TableModel {

    private final String[][] data;
    public final List<TableModelListener> listeners;

    public DataTableModel(){
        this(1, 1);
    }

    public DataTableModel(int rows, int cols) {
        data = new String[rows][cols];
        listeners = new ArrayList<>(1);
    }
    @Override
    public int getRowCount() {
        return data.length;
    }
    @Override
    public int getColumnCount() {
        return data[0].length;
    }
    @Override
    public String getColumnName(int columnIndex) {
        return null;
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            return data[rowIndex][columnIndex];
        }catch (ArrayIndexOutOfBoundsException e){
            System.err.println("ERR - nonExisting Index used for DataTableModel.getValueAt(_, _)");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        listeners.forEach(l -> l.tableChanged(new TableModelEvent(this, rowIndex, columnIndex)));
        try {
            data[rowIndex][columnIndex] = aValue.toString();
            for (TableModelListener listener:listeners) {
                listener.tableChanged(new TableModelEvent(this, rowIndex, columnIndex));
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.err.println("ERR - nonExisting Index used for CodeTableModel.setValueAt(_, _)");
            System.err.println("    - size is (" + getRowCount() + "x" + getColumnCount() + " but tried to access " + rowIndex + ", " + columnIndex);
            e.printStackTrace();
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }
}
