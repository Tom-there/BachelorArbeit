package de.hhu.cs.stups.algvis.plugins;

import de.hhu.cs.stups.algvis.data.structures.Content;
import de.hhu.cs.stups.algvis.gui.ToolBarButton;

import java.util.List;
import java.util.Set;

public interface Plugin{
  public String getName();
  public void onPluginLoad();
  public Set<Content> getGuiElements();
  public List<ToolBarButton> getToolBarButtons();
  public void refreshGuiElements();
}
