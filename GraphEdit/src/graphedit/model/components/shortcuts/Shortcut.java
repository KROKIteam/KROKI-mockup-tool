package graphedit.model.components.shortcuts;

import java.util.UUID;

import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.diagram.GraphEditModel;

public interface Shortcut {

	public GraphElement shortcutTo();
	public void setShortcutTo(LinkableElement shortcutTo);
	public GraphEditModel shortcutToModel();
	public void setShortcutToModel(GraphEditModel model);
	public void setShortcutInfo(GraphEditModel currentModel);
	public String shortcutInfo();
	public UUID shortcutId();
}
