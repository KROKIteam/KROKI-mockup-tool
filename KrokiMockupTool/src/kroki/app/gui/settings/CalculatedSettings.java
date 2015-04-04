package kroki.app.gui.settings;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.property.Calculated;
import net.miginfocom.swing.MigLayout;

/**
 * Tabbed pane with calculated property settings
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class CalculatedSettings extends VisiblePropertySettings {

	private static final long serialVersionUID = 1L;
	
	protected JLabel expressionLb;
	protected JTextArea expressionTa;
	protected JScrollPane expressionSp;

    public CalculatedSettings(SettingsCreator settingsCreator) {
        super(settingsCreator);
        initComponents();
        layoutComponents();
        addActions();
    }

    private void initComponents() {
        expressionLb = new JLabel(Intl.getValue("calculated.expression"));
        expressionTa = new JTextArea(5, 30);
        expressionTa.setFont(this.getFont());
        expressionSp = new JScrollPane(expressionTa);
        expressionSp.setMinimumSize(expressionTa.getPreferredScrollableViewportSize());
        disabledCb.setEnabled(false);
    }

    private void layoutComponents() {
        JPanel intermediate = null;
        JScrollPane pane;
        if (panelMap.containsKey("group.INTERMEDIATE")) {
            intermediate = panelMap.get("group.INTERMEDIATE");
        } else {
            intermediate = new JPanel();
            intermediate.setLayout(new MigLayout("wrap 2,hidemode 3", "[right, shrink][fill, 200]"));
            panelMap.put("group.INTERMEDIATE", intermediate);
            pane = new JScrollPane(intermediate);
            addTab(Intl.getValue("group.INTERMEDIATE"), pane);
        }
        intermediate.add(expressionLb);
        intermediate.add(expressionSp);
    }

    private void addActions() {
        expressionTa.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            public void removeUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            public void changedUpdate(DocumentEvent e) {
                //nothing
            }

            private void contentChanged(DocumentEvent e) {
                Document doc = e.getDocument();
                String text = "";
                try {
                    text = doc.getText(0, doc.getLength());
                } catch (BadLocationException ex) {
                }
                Calculated calculated = (Calculated) visibleElement;
                calculated.setExpression(text);
                updatePreformed();
            }
        });
    }

    @Override
    public void updateComponents() {
        super.updateComponents();
        Calculated calculated = (Calculated) visibleElement;
        expressionTa.setText(calculated.getExpression());
    }

    @Override
    public void updateSettings(VisibleElement visibleElement) {
        super.updateSettings(visibleElement);
    }

    @Override
    public void updatePreformed() {
        super.updatePreformed();
    }
}
