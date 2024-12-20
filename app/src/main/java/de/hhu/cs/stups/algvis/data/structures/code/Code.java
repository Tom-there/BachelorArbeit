package de.hhu.cs.stups.algvis.data.structures.code;

import de.hhu.cs.stups.algvis.data.structures.Content;

import javax.swing.*;
import java.awt.*;

public class Code implements Content {

    private final JTextPane textPane;

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

    public String getCodeInLine(int currentLine) {
        String fullCode = textPane.getText();
        String[] codeLines = fullCode.split("\n");
        return codeLines[currentLine-1];
    }

    public int getLineCount() {
        return textPane.getText().split("\n").length;
    }
}