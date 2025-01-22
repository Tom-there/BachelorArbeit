package de.hhu.cs.stups.algvis.data.structures.table;


import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeInstruction;
import de.hhu.cs.stups.algvis.data.code.threeAddressCode.ThreeAddressCodeRepresentation;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class CodeTableModel implements TableModel {

    private String[][] code;
    public final List<TableModelListener> listeners;

    public CodeTableModel(){
        code = new String[][]{{"", "", "", "", "", "", ""}};
        listeners = new ArrayList<>(1);
    }
    public void setCodeList(List<ThreeAddressCodeInstruction> param){
        listeners.forEach(l -> l.tableChanged(new TableModelEvent(this)));
        int lines = param.size();
        int columns = ThreeAddressCodeRepresentation.size();
        code = new String[lines][columns];
        for (int i = 0; i < code.length; i++) {
            ThreeAddressCodeRepresentation lineRepresentation = param.get(i).getRepresentation();
            for (int j = 0; j < code[i].length; j++){
                String cellRep = lineRepresentation.get(j);
                if(cellRep == null){
                    cellRep = "";
                    System.err.println("WRN - got a representation String that is null, replaced with empty String");
                }
                code[i][j] = cellRep;
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
        return ThreeAddressCodeInstruction.class;
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
