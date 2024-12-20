package de.hhu.cs.stups.algvis.plugins.CFGtoTAC;

import de.hhu.cs.stups.algvis.data.structures.code.Code;
import de.hhu.cs.stups.algvis.data.structures.Content;
import de.hhu.cs.stups.algvis.data.structures.graph.Graph;
import de.hhu.cs.stups.algvis.data.structures.graph.GraphMode;
import de.hhu.cs.stups.algvis.data.structures.graph.Node;
import de.hhu.cs.stups.algvis.gui.Locator;
import de.hhu.cs.stups.algvis.gui.ToolBarButton;
import de.hhu.cs.stups.algvis.plugins.LoadCodeFromFile;
import de.hhu.cs.stups.algvis.plugins.Plugin;
import de.hhu.cs.stups.algvis.plugins.SimpleSteps;
import org.checkerframework.checker.units.qual.N;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CFGtoTAC implements Plugin, SimpleSteps, LoadCodeFromFile {
    private Code tac;
    private Graph cfg;


    private boolean wasJump;
    private int currentLineNumber;
    private Node currentNode;

    public CFGtoTAC() {
        this.tac = new Code();
        this.cfg = new Graph(GraphMode.CodeInNode);
        tac.setTextRaw("empty");
    }

    @Override
    public String getName() {
        return "CFG to TAC";
    }

    @Override
    public HashMap<Locator, Content> getGuiElements() {
        HashMap<Locator, Content> ret = new HashMap<>(2);
        ret.put(Locator.left, tac);
        ret.put(Locator.center, cfg);
        return ret;
    }

    @Override
    public List<ToolBarButton> getEnabledToolBarButtons() {
        List<ToolBarButton> buttons = new LinkedList<>();
        buttons.add(LoadCodeFromFile.getButton(this));
        buttons.addAll(SimpleSteps.getButtons(this));
        return buttons;
    }

    @Override
    public void reset() {
        currentLineNumber = 1;
        cfg.purge();
    }

    @Override
    public void step() {
        if(currentLineNumber == 0){
            System.out.println("starting");
            currentLineNumber = 1;
            String currentLine = tac.getCodeInLine(currentLineNumber);
            currentNode = new Node(Integer.toString(currentLineNumber), currentLine);
            cfg.addNode(currentNode);
        }else if(currentLineNumber > tac.getLineCount()){
            System.out.println("EOF");
        }else{
            String currentLine = tac.getCodeInLine(currentLineNumber);
            //if previous line was a jump
            if(wasJump){
                //addCode to a new Node
                Node nextNode = new Node(Integer.toString(currentLineNumber), currentLine);
                cfg.addNodeWithEdges(nextNode, List.of(currentNode));
                currentNode = nextNode;
            }else{
                currentNode.setText(currentNode.getText().concat("\n" + currentLine));
            }
            //decide if current line was a jump

        }
        currentLineNumber++;
        cfg.refresh();
    }

    @Override
    public void loadCode(String code) {
        tac.setTextRaw(code);
        currentLineNumber = 0;
    }
}