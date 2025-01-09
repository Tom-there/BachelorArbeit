package de.hhu.cs.stups.algvis.gui;

import de.hhu.cs.stups.algvis.PluginManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ToolBar extends JToolBar {

    public ToolBar(PluginManager pluginManager){
        super();
    }

    public void setEnabledButtons(List<ToolBarButton> enabledButtons){
        this.removeAll();
        for (ToolBarButton button: enabledButtons){
            JButton jButton = new JButton(button.getText());
            jButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    button.action();
                }
            });
            this.add(jButton);
        }
    }
}
