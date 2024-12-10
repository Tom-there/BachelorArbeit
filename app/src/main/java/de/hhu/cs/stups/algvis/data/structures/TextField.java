package de.hhu.cs.stups.algvis.data.structures;

import javax.swing.*;
import java.awt.*;

public class TextField implements Content{

    private JTextArea textPane;

    public TextField(String text){
        textPane = new JTextArea();
        textPane.setText(text);
    }
    @Override
    public Component getSwingComponent() {
        return textPane;
    }

    public void setTextRaw(String text){
        textPane.setText(text);
    }
}