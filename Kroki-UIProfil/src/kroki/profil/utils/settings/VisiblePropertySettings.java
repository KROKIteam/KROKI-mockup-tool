/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.utils.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import kroki.intl.Intl;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.property.VisibleProperty;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class VisiblePropertySettings extends VisibleElementSettings {

	protected JLabel typelbl;
    protected JLabel columnLabelLb;
    protected JLabel displayFormatLb;
    protected JLabel representativeLb;
    protected JLabel autoGoLb;
    protected JLabel disabledLb;
    protected JLabel defaultValueLb;
    protected JComboBox<String> typeCb;
    protected JTextField columnLabelTf;
    protected JTextField displayFormatTf;
    protected JTextField defaultValueTf;
    protected JCheckBox representativeCb;
    protected JCheckBox disabledCb;
    protected JCheckBox autoGoCb;

    public VisiblePropertySettings(SettingsCreator settingsCreator) {
        super(settingsCreator);
        initComponents();
        layoutComponents();
        addActions();
    }

    private void initComponents() {
    	typelbl = new JLabel("Data type");
        columnLabelLb = new JLabel(Intl.getValue("visibleProperty.columnLabel"));
        displayFormatLb = new JLabel(Intl.getValue("visibleProperty.displayFormat"));
        representativeLb = new JLabel(Intl.getValue("visibleProperty.representative"));
        autoGoLb = new JLabel(Intl.getValue("visibleProperty.autoGo"));
        disabledLb = new JLabel(Intl.getValue("visibleProperty.disabled"));
        defaultValueLb = new JLabel(Intl.getValue("visibleProperty.defaultValue"));
        
        String[] types = { "String", "Integer", "Long", "BigDecimal", "Date" };
        typeCb = new JComboBox<String>(types);
        typeCb.setSelectedIndex(0);
        typeCb.setEnabled(false);
        
        columnLabelTf = new JTextField(30);
        displayFormatTf = new JTextField(30);
        defaultValueTf = new JTextField(30);
        representativeCb = new JCheckBox();
        autoGoCb = new JCheckBox();
        disabledCb = new JCheckBox();
    }

    private void layoutComponents() {
        JPanel intermediate = null;
        if (panelMap.containsKey("group.INTERMEDIATE")) {
            intermediate = panelMap.get("group.INTERMEDIATE");
        } else {
            intermediate = new JPanel();
            intermediate.setLayout(new MigLayout("wrap 2,hidemode 3", "[right, shrink][fill, 200]"));
            panelMap.put("group.INTERMEDIATE", intermediate);
            addTab(Intl.getValue("group.INTERMEDIATE"), intermediate);
        }
        intermediate.add(typelbl);
        intermediate.add(typeCb);
        intermediate.add(columnLabelLb);
        intermediate.add(columnLabelTf);
        intermediate.add(displayFormatLb);
        intermediate.add(displayFormatTf);
        intermediate.add(representativeLb);
        intermediate.add(representativeCb);
        intermediate.add(autoGoLb);
        intermediate.add(autoGoCb);
        intermediate.add(disabledLb);
        intermediate.add(disabledCb);
        intermediate.add(defaultValueLb);
        intermediate.add(defaultValueTf);
        //intermediate.doLayout();
    }

    private void addActions() {
    	
    	VisiblePropertySettings.this.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				updateComponents();
			}
		});
    	
    	typeCb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
				if(typeCb.isEnabled()) {
					if(typeCb.getSelectedItem() != null) {
						visibleProperty.setDataType(typeCb.getSelectedItem().toString());
					}else{
						visibleProperty.setDataType("String");
					}
				}
			}
		});
    	
        columnLabelTf.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            public void removeUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            public void changedUpdate(DocumentEvent e) {
                //nista se ne desava
            }

            private void contentChanged(DocumentEvent e) {
                Document doc = e.getDocument();
                String text = "";
                try {
                    text = doc.getText(0, doc.getLength());
                } catch (BadLocationException ex) {
                }
                VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
                visibleProperty.setColumnLabel(text);
                updatePreformed();
            }
        });

        displayFormatTf.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            public void removeUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            public void changedUpdate(DocumentEvent e) {
                //nista se ne desava
            }

            private void contentChanged(DocumentEvent e) {
                Document doc = e.getDocument();
                String text = "";
                try {
                    text = doc.getText(0, doc.getLength());
                } catch (BadLocationException ex) {
                }
                VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
                visibleProperty.setDisplayFormat(text);
                updatePreformed();
            }
        });

        defaultValueTf.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            public void removeUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            public void changedUpdate(DocumentEvent e) {
                //nista se ne desava
            }

            private void contentChanged(DocumentEvent e) {
                Document doc = e.getDocument();
                String text = "";
                try {
                    text = doc.getText(0, doc.getLength());
                } catch (BadLocationException ex) {
                }
                VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
                visibleProperty.setDefaultValue(text);
            }
        });

        representativeCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
                visibleProperty.setRepresentative(value);
                updatePreformed();
            }
        });

        autoGoCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
                visibleProperty.setAutoGo(value);
                updatePreformed();
            }
        });

        disabledCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
                visibleProperty.setDisabled(value);
                updatePreformed();
            }
        });
    }

    @Override
    public void updateComponents() {
        super.updateComponents();
        VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
        if(visibleProperty.getComponentType() == ComponentType.TEXT_FIELD) {
        	typeCb.setEnabled(true);
        	typeCb.setSelectedItem(visibleProperty.getDataType());
        }
        columnLabelTf.setText(visibleProperty.getColumnLabel());
        displayFormatTf.setText(visibleProperty.getDisplayFormat());
        defaultValueTf.setText(visibleProperty.getDefaultValue());
        representativeCb.setSelected(visibleProperty.isRepresentative());
        autoGoCb.setSelected(visibleProperty.isAutoGo());
        disabledCb.setSelected(visibleProperty.isDisabled());
    }

    @Override
    public void updateSettings(VisibleElement visibleElement) {
        super.updateSettings(visibleElement);
        VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
        columnLabelTf.setText(visibleProperty.getColumnLabel());
        //ako nije text field, ne treba podesavanje za tip
        if(!(visibleElement.getComponentType() == ComponentType.TEXT_FIELD)) {
        	typelbl.setVisible(false);
        	typeCb.setVisible(false);
        }else {
        	typelbl.setVisible(true);
        	typeCb.setVisible(true);
        }
        
    }

    @Override
    public void updatePreformed() {
        super.updatePreformed();
    }
}
