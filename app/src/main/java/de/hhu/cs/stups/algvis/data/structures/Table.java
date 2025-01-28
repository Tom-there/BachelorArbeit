package de.hhu.cs.stups.algvis.data.structures;

import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.structures.table.DataTableModel;
import de.hhu.cs.stups.algvis.data.structures.table.Mode;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;


public class Table extends JTable implements DataRepresentation {
    private DataTableModel tableModel;
    private final Location location;
    private final Mode mode;
    public Table(Location location, Mode mode){
        this.location = location;
        this.mode = mode;
        tableModel = new DataTableModel();
        setModel(tableModel);
        switch (mode){
            case code -> setSize(1, 7);
            case normal -> setSize(1, 1);
            case null -> System.err.println("ERR - while generating Code(Content visualizing). Mode was null?");
        }
        switch (location){
            case left, right -> {
                this.setMinimumSize(new Dimension(180, 480));
                this.setPreferredSize(new Dimension(400, 900));
                this.setMaximumSize(new Dimension(960, 1080));
            }
            case center -> {
                this.setMinimumSize(new Dimension(360, 480));
                this.setPreferredSize(new Dimension(1600, 900));
                this.setMaximumSize(new Dimension(1920, 1080));
            }
            case null -> System.err.println("ERR - while generating Code(Content visualizing). Locator was null?");
        }
        this.setBackground(Color.lightGray);
        resizeColumnDisplay();
    }
    public Table(){
        this(Location.center, Mode.normal);
    }
    public Table(Location location){
        this(location, Mode.normal);
    }
    public Table(Mode mode){
        this(Location.center, mode);
    }
    public void resizeTable(int rows, int cols) {
        if(rows==tableModel.getRowCount() && cols == tableModel.getColumnCount())
            return;
        tableModel = new DataTableModel(rows, cols);
        this.setModel(tableModel);
    }
    @Override
    public Location getComponentLocation() {
        return location;
    }
    @Override
    public String getToolTipText(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        super.getToolTipText(mouseEvent);
        String tip = "";
        try{
            if(rowIndex<getRowCount() && rowIndex> -1
            && colIndex<getColumnCount() && colIndex> -1)
                tip = tableModel.getValueAt(rowIndex, colIndex).toString();
            else
                tip = "";
        }catch (NullPointerException ignored){

        }
        return tip;
    }
    @Override
    public Component getSwingComponent() {
        return this;
    }
    public void resizeColumnDisplay() {
        TableColumnModel columnModel = this.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount()-1; i++) {
            int width = 0;
            for (int j = 0; j < tableModel.getRowCount(); j++) {
                TableCellRenderer renderer = this.getCellRenderer(j, i);
                Component component = this.prepareRenderer(renderer, j, i);
                width = Math.max(component.getPreferredSize().width+2, width);
            }
            columnModel.getColumn(i).setMinWidth(width);
            columnModel.getColumn(i).setMaxWidth(width);
        }
        int width = 10;
        for (int j = 0; j < tableModel.getRowCount(); j++) {
            TableCellRenderer renderer = this.getCellRenderer(j, columnModel.getColumnCount()-1);
            Component component = this.prepareRenderer(renderer, j, columnModel.getColumnCount()-1);
            width = Math.max(component.getPreferredSize().width+2, width);
        }
        columnModel.getColumn(columnModel.getColumnCount()-1).setMinWidth(width);
        columnModel.getColumn(columnModel.getColumnCount()-1).setMaxWidth(this.getMaximumSize().width);
    }
    public void setValueAt(String s, int row, int col) {
        tableModel.setValueAt(s, row, col);
    }
    public void setRowTo(String[] val, int row){
        if(val.length != tableModel.getColumnCount()){
            System.err.println("ERR - cant set row " + row + " of the Table. Length of String array mismatches amount of columns");
            return;
        }
        for (int i = 0; i < val.length; i++) {
            setValueAt(val[i], row, i);
        }
    }
    public void highlightLine(int line) {
        this.clearSelection();
        if (line == 0)
            return;
        try {
            this.addRowSelectionInterval(line, line);
        }catch (IllegalArgumentException e){
            System.err.println("ERROR - tried to highlight a line that doesnt exist(most likely)");
            System.err.println("tried to highlight line " + line);
            System.err.println("current number of lines " + tableModel.getRowCount());
        }
    }
}