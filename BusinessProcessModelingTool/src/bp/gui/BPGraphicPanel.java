package bp.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Set;

import javax.swing.JPanel;

import bp.model.data.Element;
import bp.model.data.Process;
import bp.model.graphic.util.SelectionManager;

/**
 * Canvas for drawing
 * @author Sholy
 *
 */
public class BPGraphicPanel extends JPanel{

    /**
     * 
     */
    private static final long serialVersionUID = 1788034027677048992L;

    private final BPPanel mainPanel;
    private final SelectionManager selectionManager;

    private final Process process;

    public BPGraphicPanel(final BPPanel mainPanel) {
        setBackground(Color.WHITE);
        this.selectionManager = new SelectionManager();
        this.mainPanel = mainPanel;
        this.process = mainPanel.getProcess();

        initializeMouseController();
    }

    private void initializeMouseController() {
        final MouseController controller = new MouseController(this.mainPanel);
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
    }

    public SelectionManager getSelectionManager() {
        return this.selectionManager;
    }

    public BPPanel getMainPanel() {
        return this.mainPanel;
    }

    public boolean isElementAt(final Point p) {
        for (int i = this.process.getElements().size() - 1; i >= 0; i--) {
            if (this.process.getElements().get(i).isElementAt(p)) {
                return true;
            }
        }
        return false;
    }

    public Element getElementAt(final Point p) {
        for (int i = this.process.getElements().size() - 1; i >= 0; i--) {
            final Element e = this.process.getElements().get(i);
            if (e.isElementAt(p)) {
                return e;
            }
        }
        return null;
    }

    public boolean isElementAt(final Point p, final Set<Element> ignoreElements) {
        if (ignoreElements == null) {
            return isElementAt(p);
        }
        for (int i = this.process.getElements().size() - 1; i >= 0; i--) {
            final Element e = this.process.getElements().get(i);
            if (e.isElementAt(p) && !ignoreElements.contains(e)) {
                return true;
            }
        }
        return false;
    }

    public Element getElementAt(final Point p, final Set<Element> ignoreElements) {
        if (ignoreElements == null) {
            return getElementAt(p);
        }
        for (int i = this.process.getElements().size() - 1; i >= 0; i--) {
            final Element e = this.process.getElements().get(i);
            if (e.isElementAt(p) && !ignoreElements.contains(e)) {
                return e;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;

        // g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (final Element e : this.process.getElements()) {
            e.getComponent().getPainter().paint(g2);
        }

        for (final Element e : this.process.getElements()) {
            if (getSelectionManager().isElementSelected(e)) {
                e.getComponent().getPainter().paintHandlers(g2);
            }
        }
    }

}
