package de.hhu.cs.stups.algvis.pluginSpecs;

import java.util.List;

public interface SimpleSteps {
    void reset();
    void step();
    void run();

    static List<ToolBarButton> getButtons(SimpleSteps impl){
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
        }, new ToolBarButton() {
            @Override
            public String getText() {
                return "run";
            }

            @Override
            public void action() {
                impl.run();
            }
        });
    }
}
