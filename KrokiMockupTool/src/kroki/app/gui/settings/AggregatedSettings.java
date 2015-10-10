package kroki.app.gui.settings;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.property.AggregateFunciton;
import kroki.profil.property.Aggregated;
import net.miginfocom.swing.MigLayout;

/**
 * Tabbed pane with aggregated property settings
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AggregatedSettings extends VisiblePropertySettings {

	private static final long serialVersionUID = 1L;
	
    protected JLabel functionLb;
    protected JLabel selectionLb;
    protected JLabel aggregatingAttributeLb;
    protected JComboBox functionCb;
    protected JTextArea selectionTa;
    protected JScrollPane selectionSp;
    protected JTextField aggregatingAttributeTf;
    protected JButton aggregatingAttrbuteBtn;

    public AggregatedSettings(SettingsCreator settingsCreator) {
        super(settingsCreator);
        initComponents();
        layoutComponents();
        addActions();
    }

    private void initComponents() {
        functionLb = new JLabel(Intl.getValue("aggregated.function"));
        selectionLb = new JLabel(Intl.getValue("aggregated.selection"));
        aggregatingAttributeLb = new JLabel(Intl.getValue("aggregated.aggregatingAttribute"));

        functionCb = new JComboBox(AggregateFunciton.values());
        selectionTa = new JTextArea(5, 30);
        selectionTa.setFont(this.getFont());
        selectionSp = new JScrollPane(selectionTa);
        selectionSp.setMinimumSize(selectionTa.getPreferredScrollableViewportSize());
        aggregatingAttributeTf = new JTextField();
        aggregatingAttributeTf.setEditable(false);
        aggregatingAttrbuteBtn = new JButton("...");
        aggregatingAttrbuteBtn.setPreferredSize(new Dimension(30, 20));
        aggregatingAttrbuteBtn.setMinimumSize(aggregatingAttrbuteBtn.getPreferredSize());

        
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
        intermediate.add(functionLb);
        intermediate.add(functionCb);
        intermediate.add(selectionLb);
        intermediate.add(selectionSp);
        intermediate.add(aggregatingAttributeLb);
        intermediate.add(aggregatingAttributeTf, "split 2, grow");
        intermediate.add(aggregatingAttrbuteBtn, "shrink");
    }

    private void addActions() {
        functionCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                Object value = comboBox.getSelectedItem();
                Aggregated aggregated = (Aggregated) visibleElement;
                aggregated.setFunciton((AggregateFunciton) value);
            }
        });

        selectionTa.getDocument().addDocumentListener(new DocumentListener() {

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
                Aggregated aggregated = (Aggregated) visibleElement;
                aggregated.setSelection(text);
                updatePreformed();
            }
        });

        aggregatingAttrbuteBtn.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Unsuported yet");
            }
        });

    }

    @Override
    public void updateComponents() {
        super.updateComponents();
        Aggregated aggregated = (Aggregated) visibleElement;
        functionCb.setSelectedItem(aggregated.getFunciton());
        selectionTa.setText(aggregated.getSelection());
        String aggrAttrValue = "";
        if (aggregated.getAggregatingAttribute() != null) {
            aggrAttrValue = aggregated.getAggregatingAttribute().toString();
        }
        aggregatingAttributeTf.setText(aggrAttrValue);
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
