package de.hhu.cs.stups.algvis.data.structures;

import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.structures.table.DataTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;


public class Table extends JTable implements DataRepresentation {
    private DataTableModel tableModel;
    private final Location location;
    public Table(Location location){
        this.location = location;
        tableModel = new DataTableModel();
        setModel(tableModel);
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
            case null, default -> System.err.println("ERROR, while generating Code(Content visualizing). Locator parameter was not able to be interpreted");
        }
        this.setBackground(Color.lightGray);
        resizeColumns();
    }
    public Table(){
        this(Location.center);
    }

    public void resizeTable(int rows, int cols) {
        tableModel = new DataTableModel(rows, cols);
        this.setModel(tableModel);
    }
    public DataTableModel getTableModel(){
        return tableModel;
    }@Override

    public Location getComponentLocation() {
        return location;
    }

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


    private void resizeColumns() {
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
}