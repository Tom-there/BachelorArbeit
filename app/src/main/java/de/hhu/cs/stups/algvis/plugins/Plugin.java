package de.hhu.cs.stups.algvis.plugins;

import de.hhu.cs.stups.algvis.data.structures.Content;
import de.hhu.cs.stups.algvis.gui.Locator;
import de.hhu.cs.stups.algvis.gui.ToolBarButton;

import java.util.HashMap;
import java.util.List;

public interface Plugin{
  public String getName();
  public HashMap<Locator, Content> getGuiElements();
  public List<ToolBarButton> getEnabledToolBarButtons();
}
