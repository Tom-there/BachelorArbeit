package de.hhu.cs.stups.algvis.gui;

import de.hhu.cs.stups.algvis.plugins.Plugin;
import de.hhu.cs.stups.algvis.data.structures.Content;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ContentPanel extends JPanel {

    public ContentPanel(){
        super();
        setMinimumSize(new Dimension(720, 480));
        setPreferredSize(new Dimension(1600, 900));
        setMaximumSize(new Dimension(1920, 1080));
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        add(genSplashScreen(), BorderLayout.CENTER);

    }

    public void switchToPlugin(Plugin plugin){
        removeAll();
        for (Content content : plugin.getGuiElements()) {
            switch (content.getLocation()){
                case left -> add(content.getSwingComponent(), BorderLayout.WEST);
                case right -> add(content.getSwingComponent(), BorderLayout.EAST);
                case center -> add(content.getSwingComponent(), BorderLayout.CENTER);
            }
        }
        setVisible(true);
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
