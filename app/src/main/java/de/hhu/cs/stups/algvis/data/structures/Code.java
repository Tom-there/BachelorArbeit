package de.hhu.cs.stups.algvis.data.structures;

import javax.swing.*;
import java.awt.*;

public class Code implements Content{

    private JTextPane textPane;

    public Code(){
        textPane = new JTextPane();
        textPane.setBackground(Color.lightGray);
    }
    @Override
    public Component getSwingComponent() {
        return textPane;
    }

    public void setTextRaw(String text){
        textPane.setText(text);
    }

    public void highlightLine(int currentLine) {
        System.out.println("not implemented");
    }
}