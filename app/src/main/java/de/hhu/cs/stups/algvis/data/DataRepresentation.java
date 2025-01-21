package de.hhu.cs.stups.algvis.data;

import java.awt.Component;

public interface DataRepresentation {
    enum Location{left, right, center}
    Location getLocation();
    Component getSwingComponent();
}