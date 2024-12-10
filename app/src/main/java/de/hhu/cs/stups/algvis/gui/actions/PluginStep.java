package de.hhu.cs.stups.algvis.gui.actions;

import de.hhu.cs.stups.algvis.gui.ContentPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PluginStep implements ActionListener {

    private final ContentPanel contentPanel;
    public PluginStep(ContentPanel contentPanel){
        this.contentPanel = contentPanel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        contentPanel.getCurrentPlugin().doStep();
    }
}
