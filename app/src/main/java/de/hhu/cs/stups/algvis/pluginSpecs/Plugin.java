package de.hhu.cs.stups.algvis.pluginSpecs;

import de.hhu.cs.stups.algvis.data.DataRepresentation;

import java.util.Collection;

public interface Plugin{
  String getName();
  void onPluginLoad();
  Collection<DataRepresentation> getGuiElements();
  Collection<ToolBarButton> getToolBarButtons();
  void refreshGuiElements();
}
