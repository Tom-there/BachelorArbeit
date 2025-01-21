package de.hhu.cs.stups.algvis.data.structures.table;


import de.hhu.cs.stups.algvis.data.ThreeAddressCodeInstruction;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class DataTableModel implements TableModel {

    private String[][] data;
    public List<TableModelListener> listeners;

    public DataTableModel(){
        this(1, 1);
    }

    public DataTableModel(int rows, int cols) {
        data = new String[rows][cols];
        listeners = new ArrayList<>(1);
    }

    public void setCodeList(List<ThreeAddressCodeInstruction> param){
        listeners.forEach(l -> l.tableChanged(new TableModelEvent(this)));
        int lines = param.size();
        int columns = ThreeAddressCodeInstruction.TACRepresentation.size();
        data = new String[lines][columns];
        for (int i = 0; i < data.length; i++) {
            ThreeAddressCodeInstruction.TACRepresentation lineRepresentation = param.get(i).getRepresentation();
            for (int j = 0; j < data[i].length; j++){
                String cellRep = lineRepresentation.get(j);
                if(cellRep == null){
                    cellRep = "";
                    System.err.println("WRN - got a representation String that is null, replaced with empty String");
                }
                data[i][j] = cellRep;
            }
        }
    }
    @Override
    public int getRowCount() {
        return data.length;
    }

    public void setRowCount(int rowCount){
        if (rowCount<getRowCount())
            return;
        String[][] oldData = data;
        int colCount = oldData[0].length;
        data = new String[rowCount][colCount];
        for (int i = 0; i < oldData.length; i++) {
            for (int j = 0; j < oldData[i].length; j++) {
                data[i][j] = oldData[i][j];
            }
        }
        for (TableModelListener listener:listeners) {
            listener.tableChanged(new TableModelEvent(this));
        }
    }
    @Override
    public int getColumnCount() {
        return data[0].length;
    }
    public void setColumnCount(int colCount){
        if (colCount < getColumnCount())
            return;
        String[][] oldData = data;
        int rowCount = oldData.length;
        data = new String[rowCount][colCount];
        for (int i = 0; i < oldData.length; i++) {
            for (int j = 0; j < oldData[i].length; j++) {
                data[i][j] = oldData[i][j];
            }
        }
        for (TableModelListener listener:listeners) {
            listener.tableChanged(new TableModelEvent(this));
        }
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

    public int getMaxCharLengthForColumn(int j) {
        int longest = 0;
        for (String[] row : data) {
            int current = 0;
            try {
                current = row[j].length();
            } catch (NullPointerException e) {
                System.err.println("WRN - Tried accessing a String that is null in CodeTableModel.getMaxCharLengthForColumn(...)");
            }
            longest = Math.max(longest, current);
        }
        return longest;
    }
}
