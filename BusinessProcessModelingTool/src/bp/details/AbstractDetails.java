package bp.details;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

public class AbstractDetails extends JTabbedPane {

    /**
     * 
     */
    private static final long serialVersionUID = -2816788634630499565L;

    private JPanel basic;
    private JPanel advanced;
    
    public AbstractDetails() {

    }

    public JPanel getBasic() {
        return basic;
    }

    public JPanel getAdvanced() {
        return advanced;
    }

    public void createBasic() {
        if (basic == null) {
            basic = new JPanel();
            basic.setLayout(new MigLayout("wrap 2", "[right][left]", "[top][top]"));
            addTab("Basic", basic);
        }
    }

    public void createAdvanced() {
        if (advanced == null) {
            advanced = new JPanel();
            advanced.setLayout(new MigLayout("wrap 2", "[right][left]", "[top][top]"));
            addTab("Advanced", advanced);
        }
    }
    
    
}
