package de.hhu.cs.stups.algvis.plugins;

import de.hhu.cs.stups.algvis.gui.ToolBarButton;

public interface LoadCodeFromFile {
    public void loadCode(String s);
    public static ToolBarButton getButton(LoadCodeFromFile impl){
        return new ToolBarButton() {
            @Override
            public String getText() {
                return "load";
            }

            @Override
            public void action() {
                impl.loadCode(testTAC());
            }
        };
    }


    private static String testTAC(){
        return """
                LD R0\ni LD R1\n#4 MUL R0\nR0\nR1 LD R1\na(R0) ST x\nR1 LD R0\nj LD R1\n#4 MUL R0\nR0\nR1 LD R1\nb(R0) ST y\nR1 LD R0\ny LD R1\ni LD R2\n#4 MUL R1\nR1\nR2 ST a(R1)\nR0 LD R0\nx LD R1\nj LD R2\n#4 MUL R1\nR1\nR2 ST b(R1)\nR0
                """;
    }
}
