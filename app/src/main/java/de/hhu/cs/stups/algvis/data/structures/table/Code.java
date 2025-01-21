package de.hhu.cs.stups.algvis.data.structures.table;

import de.hhu.cs.stups.algvis.data.ThreeAddressCode;
import de.hhu.cs.stups.algvis.data.DataRepresentation;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;


public class Code implements DataRepresentation {
    private final JTable table;
    private final CodeTableModel tableModel;
    private final DataRepresentation.Location location;
    public Code(DataRepresentation.Location location){
        this.location = location;
        tableModel = new CodeTableModel();
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



    public Code(){
        this(DataRepresentation.Location.center);
    }
    @Override
    public Component getSwingComponent() {
        return table;
    }
    @Override
    public DataRepresentation.Location getLocation() {
        return location;
    }

    public void setCode(ThreeAddressCode code){
        if(code == null){
            System.err.println("ERROR - Code.setCode(null) ???"); //todo
        }else {
            tableModel.setCodeList(code.getInstructions());
            resizeColumns();
        }
    }

    public void highlightLine(int line) {
        table.clearSelection();
        if (line == 0)
            return;
        try {
            table.addRowSelectionInterval(line - 1, line - 1);
        }catch (IllegalArgumentException e){
            System.err.println("ERROR - tried to highlight a line that doesnt exist(most likely)");
            System.err.println("tried to highlight line " + line);
            System.err.println("current number of lines " + tableModel.getRowCount());
        }
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
}