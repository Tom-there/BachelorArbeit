package de.hhu.cs.stups.algvis.gui.actions;

import de.hhu.cs.stups.algvis.gui.ContentPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PluginReset implements ActionListener {
    private final ContentPanel contentPanel;
    public PluginReset(ContentPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        contentPanel.getCurrentPlugin().reset();
    }
}
