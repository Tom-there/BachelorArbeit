package de.hhu.cs.stups.algvis.data.structures;

import de.hhu.cs.stups.algvis.gui.Locator;

import java.awt.*;

public interface Content {
    public Component getSwingComponent();
    public Locator getLocation();
}