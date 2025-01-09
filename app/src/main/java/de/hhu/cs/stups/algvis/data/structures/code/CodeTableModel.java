package de.hhu.cs.stups.algvis.data.structures.code;


import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.maxBy;

public class CodeTableModel implements TableModel {

    private String[][] code;
    public List<TableModelListener> listeners;

    public CodeTableModel(){
        code = new String[][]{{"", "", "", "", "", "", ""}};
        listeners = new ArrayList<>(1);
    }
    public void setCodeList(List<ThreeAddressCode> param){
        listeners.forEach(l -> l.tableChanged(new TableModelEvent(this)));
        code = new String[param.size()][ThreeAddressCode.TACRepresentation.size()+1];
        for (int i = 0; i < code.length; i++) {
            code[i][0] = Integer.toString(i);
            for (int j = 0; j < code[i].length-1; j++){
                String rep = param.get(i).getRepresentation().get(j);
                if(rep == null){
                    rep = "";
                    System.err.println("WRN - got a representation String that is null, replaced with empty String");
                }
                code[i][j+1] = rep;
            }
        }
    }
    @Override
    public int getRowCount() {
        return code.length;
    }

    @Override
    public int getColumnCount() {
        return code[0].length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return "Code";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ThreeAddressCode.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            return code[rowIndex][columnIndex];
        }catch (ArrayIndexOutOfBoundsException e){
            System.err.println("ERR - nonExisting Index used for CodeTableModel.getValueAt(...)");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    //should be unused
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        listeners.forEach(l -> l.tableChanged(new TableModelEvent(this, rowIndex, columnIndex)));
        System.err.println("WRN - unexpected use of CodeTableModel.setValueAt()");
        if(columnIndex!=5){
            System.err.println("ERR - CodeTableModel.setValueAt(_, _, " + columnIndex + ") only comment(Column 5) is editable");
            Thread.dumpStack();
            return;
        }
        if(!(aValue instanceof String)){
            System.err.println("ERR - CodeTableModel.setValueAt( val, _, 5) value should be String but is " + aValue.getClass().getName());
        }
        try {
            code[rowIndex][5] = aValue.toString();
        }catch (ArrayIndexOutOfBoundsException e){
            System.err.println("ERR - nonExisting Index used for CodeTableModel.setValueAt(...)");
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
        for (String[] row : code) {
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
