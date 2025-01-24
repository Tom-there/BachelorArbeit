package de.hhu.cs.stups.algvis.data;

import java.awt.Component;

public interface DataRepresentation {
    Location getComponentLocation();
    enum Location{left, right, center}
    Component getSwingComponent();
}