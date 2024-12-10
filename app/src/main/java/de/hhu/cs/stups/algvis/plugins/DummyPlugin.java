package de.hhu.cs.stups.algvis.plugins;

import de.hhu.cs.stups.algvis.data.structures.Content;
import de.hhu.cs.stups.algvis.data.structures.TextField;
import de.hhu.cs.stups.algvis.gui.Locator;

import java.util.HashMap;

public class DummyPlugin implements Plugin {

    public DummyPlugin(){
    }
    @Override
    public String getName() {
        return "Dummy";
    }

    @Override
    public HashMap<Locator, Content> getGuiElements() {
        HashMap<Locator, Content> ret = new HashMap<>();
        ret.put(Locator.center, new TextField("Dummy"));
        return ret;
    }

    @Override
    public void reset() {

    }

    @Override
    public void doStep() {}
}