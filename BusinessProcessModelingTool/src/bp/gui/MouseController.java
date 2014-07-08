package bp.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseController extends MouseAdapter {

    private final BPPanel panel;
    
    public MouseController(BPPanel panel) {
        this.panel = panel;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        panel.getStateManager().getCurrentState().mousePressed(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
            panel.getStateManager().getCurrentState().mouseDoubleClick(e);
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        panel.getStateManager().getCurrentState().mouseReleased(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        panel.getStateManager().getCurrentState().mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        panel.getStateManager().getCurrentState().mouseMoved(e);
    }
    

}
