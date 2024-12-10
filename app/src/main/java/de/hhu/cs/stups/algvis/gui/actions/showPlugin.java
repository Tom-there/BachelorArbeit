package de.hhu.cs.stups.algvis.gui.actions;

import de.hhu.cs.stups.algvis.plugins.Plugin;
import de.hhu.cs.stups.algvis.gui.ContentPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class showPlugin extends AbstractAction {
    private final Plugin plugin;
    private final ContentPanel contentPanel;
    private final JFrame frame;
    public showPlugin(Plugin plugin, ContentPanel contentPanel, JFrame frame){
        this.plugin = plugin;
        this.contentPanel = contentPanel;
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        contentPanel.switchToPlugin(plugin);
        frame.setVisible(true);
    }
}
