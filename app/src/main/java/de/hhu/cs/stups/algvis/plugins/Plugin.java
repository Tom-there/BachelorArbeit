package de.hhu.cs.stups.algvis.plugins;

import de.hhu.cs.stups.algvis.data.structures.Content;
import de.hhu.cs.stups.algvis.gui.Locator;

import java.util.HashMap;

public interface Plugin{
  public void reset();
  public void doStep();
  public String getName();
  public HashMap<Locator, Content> getGuiElements();
}
