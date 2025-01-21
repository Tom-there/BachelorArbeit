package de.hhu.cs.stups.algvis.data.structures;

import de.hhu.cs.stups.algvis.data.DataRepresentation;
import de.hhu.cs.stups.algvis.data.structures.table.DataTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;


public class Table implements DataRepresentation {
    private final JTable table;
    private DataTableModel tableModel;
    private final Location location;
    public Table(Location location){
        this.location = location;
        tableModel = new DataTableModel();
        table = new JTable(tableModel);
        switch (location){
            case left, right -> {
                table.setMinimumSize(new Dimension(180, 480));
                table.setPreferredSize(new Dimension(400, 900));
                table.setMaximumSize(new Dimension(960, 1080));
            }
            case center -> {
                table.setMinimumSize(new Dimension(360, 480));
                table.setPreferredSize(new Dimension(1600, 900));
                table.setMaximumSize(new Dimension(1920, 1080));
            }
            case null, default -> System.err.println("ERROR, while generating Code(Content visualizing). Locator parameter was not able to be interpreted");
        }
        table.setBackground(Color.lightGray);
        resizeColumns();
    }
    public Table(){
        this(Location.center);
    }

    public void resizeTable(int rows, int cols) {
        tableModel = new DataTableModel(rows, cols);
        table.setModel(tableModel);
    }
    public DataTableModel getTableModel(){
        return tableModel;
    }
    @Override
    public Component getSwingComponent() {
        return table;
    }
    @Override
    public Location getLocation() {
        return location;
    }

    private void resizeColumns() {
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount()-1; i++) {
            int width = 0;
            for (int j = 0; j < tableModel.getRowCount(); j++) {
                TableCellRenderer renderer = table.getCellRenderer(j, i);
                Component component = table.prepareRenderer(renderer, j, i);
                width = Math.max(component.getPreferredSize().width+2, width);
            }
            columnModel.getColumn(i).setMinWidth(width);
            columnModel.getColumn(i).setMaxWidth(width);
        }
        int width = 10;
        for (int j = 0; j < tableModel.getRowCount(); j++) {
            TableCellRenderer renderer = table.getCellRenderer(j, columnModel.getColumnCount()-1);
            Component component = table.prepareRenderer(renderer, j, columnModel.getColumnCount()-1);
            width = Math.max(component.getPreferredSize().width+2, width);
        }
        columnModel.getColumn(columnModel.getColumnCount()-1).setMinWidth(width);
        columnModel.getColumn(columnModel.getColumnCount()-1).setMaxWidth(table.getMaximumSize().width);
    }

    public void setValueAt(String s, int row, int col) {
        tableModel.setValueAt(s, row, col);
    }
}