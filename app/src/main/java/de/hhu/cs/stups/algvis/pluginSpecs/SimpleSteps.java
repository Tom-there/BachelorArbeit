package de.hhu.cs.stups.algvis.pluginSpecs;

import java.util.List;

public interface SimpleSteps {
    void reset();
    void step();
    boolean hasNextStep();

    static List<ToolBarButton> getButtons(SimpleSteps impl){
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
