package bp.state;

import bp.gui.BPGraphicPanel;
import bp.gui.BPPanel;
import bp.gui.BPTextPanel;

public abstract class BPState extends State {

    private final BPPanel panel;

    public BPState(BPPanel panel) {
        this.panel = panel;
    }

    public BPPanel getPanel() {
        return panel;
    }

    public BPGraphicPanel getGraphicPanel() {
        return panel.getGraphicsPanel();
    }

    public BPTextPanel getTextPanel() {
        return panel.getText();
    }
}
