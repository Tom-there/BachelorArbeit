package de.hhu.cs.stups.algvis.pluginSpecs.toolBarButtons;

import de.hhu.cs.stups.algvis.pluginSpecs.ToolBarButton;

import java.util.Collection;
import java.util.List;

public interface SimpleSteps {
    void reset();
    void step();
    boolean hasNextStep();
    static Collection<ToolBarButton> getButtons(SimpleSteps impl){
        return List.of(new ToolBarButton() {
            @Override
            public String getText() {
                return "step";
            }

            @Override
            public void action() {
                if(impl.hasNextStep())
                    impl.step();
                else
                    System.out.println("At end of Algo");
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
                while (impl.hasNextStep())
                    impl.step();
            }
        });
    }
}
