package de.hhu.cs.stups.algvis.gui;

import de.hhu.cs.stups.algvis.plugins.Plugin;
import de.hhu.cs.stups.algvis.data.structures.Content;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ContentPanel extends JPanel {

    private Plugin currentPlugin;
    public ContentPanel(){
        currentPlugin = null;
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        add(genSplashScreen(), BorderLayout.CENTER);
    }

    public void switchToPlugin(Plugin plugin){
        removeAll();
        for (Map.Entry<Locator, Content> entry : plugin.getGuiElements().entrySet()) {
            switch (entry.getKey()){
                case left -> {
                    add(entry.getValue().getSwingComponent(), BorderLayout.WEST);
                }
                case right -> {
                    add(entry.getValue().getSwingComponent(), BorderLayout.EAST);
                }
                case center -> {
                    add(entry.getValue().getSwingComponent(), BorderLayout.CENTER);
                }
            }
        }
        setVisible(true);
        currentPlugin = plugin;
    }

    public Plugin getCurrentPlugin() {
        return currentPlugin;
    }

    //// PRIVATE FUNCTIONS \\\\
    private static JPanel genSplashScreen(){
        JPanel splashScreen = new JPanel();
        splashScreen.setBackground(Color.lightGray);
        JLabel text = new JLabel("welcome");
        splashScreen.add(text);
        return splashScreen;
    }
}
