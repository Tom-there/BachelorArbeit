package de.hhu.cs.stups.algvis.plugins;

import de.hhu.cs.stups.algvis.data.structures.Code;
import de.hhu.cs.stups.algvis.data.structures.Content;
import de.hhu.cs.stups.algvis.data.structures.Graph;
import de.hhu.cs.stups.algvis.gui.Locator;
import de.hhu.cs.stups.algvis.gui.ToolBarButtons;

import java.util.HashMap;
import java.util.List;

public class CFGtoTAC implements Plugin {
    private Code tac;
    private Graph cfg;

    private int currentLine;

    public CFGtoTAC() {
        reset();
    }

    @Override
    public String getName() {
        return "CFG aus TAC";
    }

    @Override
    public HashMap<Locator, Content> getGuiElements() {
        HashMap<Locator, Content> ret = new HashMap<>(2);
        ret.put(Locator.left, tac);
        ret.put(Locator.center, cfg);
        return ret;
    }

    @Override
    public List<ToolBarButtons> getEnabledToolBarButtons() {
        return List.of(ToolBarButtons.load, ToolBarButtons.reset, ToolBarButtons.step);
    }

    @Override
    public void reset() {
        this.tac = new Code();
        this.cfg = new Graph();
        currentLine = 1;
        tac.highlightLine(currentLine);
    }

    @Override
    public void doStep() {
    }

    private static String testTAC(){
        return """
                LD R0\ni LD R1\n#4 MUL R0\nR0\nR1 LD R1\na(R0) ST x\nR1 LD R0\nj LD R1\n#4 MUL R0\nR0\nR1 LD R1\nb(R0) ST y\nR1 LD R0\ny LD R1\ni LD R2\n#4 MUL R1\nR1\nR2 ST a(R1)\nR0 LD R0\nx LD R1\nj LD R2\n#4 MUL R1\nR1\nR2 ST b(R1)\nR0
                """;
    }
}