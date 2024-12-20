package de.hhu.cs.stups.algvis.plugins;

import de.hhu.cs.stups.algvis.gui.ToolBarButton;

import java.util.List;
import java.util.SortedMap;

public interface SimpleSteps {
    public void reset();
    public void step();

    public static List<ToolBarButton> getButtons(SimpleSteps impl){
        return List.of(new ToolBarButton() {
            @Override
            public String getText() {
                return "step";
            }

            @Override
            public void action() {
                impl.step();
            }
        }, new ToolBarButton() {
            @Override
            public String getText() {
                return "reset";
            }

            @Override
            public void action() {
                impl.reset();
            }
        });
    }
}
