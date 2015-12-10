package kroki.app.gui.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import kroki.commons.camelcase.NamingUtil;
import kroki.commons.document.OnlyDigitsDocumentFilter;
import kroki.intl.Intl;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.property.VisibleProperty;
import net.miginfocom.swing.MigLayout;

/**
 * Tabbed pane showing visible property settings.
 * It is extended by all settings classes corresponding to elements
 * which extend <code>VisibleProperty</code>. 
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class VisiblePropertySettings extends VisibleElementSettings {

	private static final long serialVersionUID = 1L;
	
	protected JLabel typelbl;
    protected JLabel columnLabelLb;
    protected JLabel labelToCodeLb;
    protected JLabel displayFormatLb;
    protected JLabel valuesLb;
    protected JLabel representativeLb;
    protected JLabel mandatoryLb;
    protected JLabel autoGoLb;
    protected JLabel disabledLb;
    protected JLabel defaultValueLb;
    protected JLabel lengthLb;
    protected JLabel precisionLb;
    protected JLabel peristentTypeLb;
    protected JComboBox<String> typeCb;
    protected JTextField columnLabelTf;
    protected JCheckBox labelToCodeCb;
    protected JTextField displayFormatTf;
    protected JTextField lengthTf;
    protected JTextField precisionTf;
    protected JComboBox<String> persistentTypeCb;
    protected ComboBoxValuesPanel valuesPanel;
    protected JTextField defaultValueTf;
    protected JCheckBox representativeCb;
    protected JCheckBox mandatoryCb;
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
    	labelToCodeLb = new JLabel(Intl.getValue("visibleProperty.labelToCode"));
        columnLabelLb = new JLabel(Intl.getValue("visibleProperty.columnLabel"));
        displayFormatLb = new JLabel(Intl.getValue("visibleProperty.displayFormat"));
        valuesLb = new JLabel(Intl.getValue("visibleProperty.values"));
        representativeLb = new JLabel(Intl.getValue("visibleProperty.representative"));
        mandatoryLb = new JLabel(Intl.getValue("visibleProperty.mandatory"));
        autoGoLb = new JLabel(Intl.getValue("visibleProperty.autoGo"));
        disabledLb = new JLabel(Intl.getValue("visibleProperty.disabled"));
        defaultValueLb = new JLabel(Intl.getValue("visibleProperty.defaultValue"));
        lengthLb = new JLabel(Intl.getValue("visibleProperty.length"));
        precisionLb = new JLabel(Intl.getValue("visibleProperty.precision"));
        peristentTypeLb = new JLabel(Intl.getValue("visibleProperty.persistentType"));
        String[] types = { "String", "Integer", "Long", "BigDecimal", "Date", "e-mail"};
        typeCb = new JComboBox<String>(types);
        typeCb.setSelectedIndex(0);
        typeCb.setEnabled(false);
        
        String[] peristentTypes = { "Char", "Varchar", "Text", "Integer", "Number", "Float", "Decimal", "Boolean", "Date", "Time", "DateTime"};
        persistentTypeCb = new JComboBox<String>(peristentTypes);
        persistentTypeCb.setSelectedItem("Varchar");
        persistentTypeCb.setSelectedIndex(0);
        System.out.println(persistentTypeCb.getSelectedItem());
        
        columnLabelTf = new JTextField(30);
        labelToCodeCb = new JCheckBox();
        displayFormatTf = new JTextField(30);
        valuesPanel = new ComboBoxValuesPanel(this, (VisibleProperty) visibleElement);
        valuesPanel.setVisibleProperty((VisibleProperty)visibleElement);
        valuesPanel.setFont(this.getFont());
        defaultValueTf = new JTextField(30);
        representativeCb = new JCheckBox();
        mandatoryCb = new JCheckBox();
        autoGoCb = new JCheckBox();
        disabledCb = new JCheckBox();
        precisionTf = new JTextField(20);
        precisionTf.setEnabled(false);
        lengthTf = new JTextField(20);
        AbstractDocument precisionDocument = (AbstractDocument) precisionTf.getDocument();
        precisionDocument.setDocumentFilter(new OnlyDigitsDocumentFilter());
        AbstractDocument lengthDocument = (AbstractDocument) lengthTf.getDocument();
        lengthDocument.setDocumentFilter(new OnlyDigitsDocumentFilter());
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
        
        JPanel persistent = null;
        JScrollPane persistentPane;
		if(panelMap.containsKey("group.PERSISTENT")) {
			persistent = panelMap.get("group.PERSISTENT");
		}else {
			persistent = new JPanel();
			persistent.setLayout(new MigLayout("wrap 2,hidemode 3", "[right, shrink][fill, 200]"));
			persistentPane = new JScrollPane(persistent);
			addTab("Persistent", persistentPane);
			panelMap.put("group.PERSISTENT", persistent);
		}
		
        intermediate.add(typelbl);
        intermediate.add(typeCb);
        intermediate.add(displayFormatLb);
        intermediate.add(displayFormatTf);
        intermediate.add(valuesLb);
        intermediate.add(valuesPanel, "height ::100");
        intermediate.add(mandatoryLb);
        intermediate.add(mandatoryCb);
        intermediate.add(representativeLb);
        intermediate.add(representativeCb);
        intermediate.add(autoGoLb);
        intermediate.add(autoGoCb);
        intermediate.add(disabledLb);
        intermediate.add(disabledCb);
        intermediate.add(defaultValueLb);
        intermediate.add(defaultValueTf);
        //intermediate.doLayout();
        
        persistent.add(labelToCodeLb);
        persistent.add(labelToCodeCb);
        persistent.add(columnLabelLb);
        persistent.add(columnLabelTf);
        persistent.add(peristentTypeLb);
        persistent.add(persistentTypeCb);
        
        persistent.add(lengthLb);
        persistent.add(lengthTf);
        persistent.add(precisionLb);
        persistent.add(precisionTf);
    }

    private void addActions() {
    	VisiblePropertySettings.this.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
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
    	
    	persistentTypeCb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
				if(persistentTypeCb.isEnabled()) {
					if(persistentTypeCb.getSelectedItem() != null) {
						visibleProperty.setPersistentType(persistentTypeCb.getSelectedItem().toString());
					}
					updateLengthAndPrecision(visibleProperty);
				}
			}
		});    	
    	
    	
    	labelToCodeCb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
				if(value) {
					visibleProperty.setLabelToCode(true);
				    columnLabelTf.setEditable(false);
				    NamingUtil namer = new NamingUtil();
					visibleProperty.setColumnLabel(namer.toDatabaseFormat(visibleProperty.umlClass().name().replace("_", " "), labelTf.getText().trim()));
					columnLabelTf.setText(visibleProperty.getColumnLabel());
				}else {
					visibleProperty.setLabelToCode(false);
					columnLabelTf.setEditable(true);
				}
				updatePreformed();
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
                //nothing
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
                //nothing
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

        valuesPanel.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				VisibleProperty prop = (VisibleProperty) visibleElement;
				String[] values = valuesPanel.getValuesAsArray();
				String enumeration = "";
				for(int i=0; i<values.length; i++) {
					enumeration +=values[i] + ";";
				}
				prop.setEnumeration(enumeration);
				updatePreformed();
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
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
                //nothing
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

        mandatoryCb.addActionListener(new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
				if(value) {
					visibleProperty.setLower(1);
				}else {
					visibleProperty.setLower(0);
				}
				updatePreformed();
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
        
        lengthTf.getDocument().addDocumentListener(new DocumentListener() {

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
                int intValue = 0;
                if (text.length() > 0)
                	intValue = Integer.parseInt(text);
                visibleProperty.setLength(intValue);
            }
        });
        
        precisionTf.getDocument().addDocumentListener(new DocumentListener() {

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
                int intValue = 0;
                if (text.length() > 0)
                	intValue = Integer.parseInt(text);
                visibleProperty.setPrecision(intValue);
            }
        });
    }
    
    /*
    private void setDefaultPersistentType() {
    	if (persistentTypeCb.getSelectedItem() != null)
    			return;
    	String type;
		if( typeCb.getSelectedItem() != null) {
			type = typeCb.getSelectedItem().toString();
		} else 
			return;
		
		switch (type) {
		case "String":
			persistentTypeCb.setSelectedItem("Varchar");
			break;
		case "Integer":
			persistentTypeCb.setSelectedItem("Integer");
			break;
		case "Long":
			persistentTypeCb.setSelectedItem("Number");			
			break;
		case "BigDecimal":
			persistentTypeCb.setSelectedItem("Decimal");			
			break;
		case "Date":
			persistentTypeCb.setSelectedItem("Date");			
			break;
		}
    }
    */
    
    private void updateLengthAndPrecision(VisibleProperty visibleProperty) {
    	String persType;
		if( persistentTypeCb.getSelectedItem() != null) {
			persType = persistentTypeCb.getSelectedItem().toString();
		}else {
			persType = "";
		}
	    switch (persType) {
	    case "Char": case "Varchar": case "Text": case "Number":
	    	lengthTf.setEnabled(true);
	    	precisionTf.setEnabled(false);
	    	visibleProperty.setPrecision(0);
	    	precisionTf.setText("0");
	    	break;
	    case "Decimal":
	    	lengthTf.setEnabled(true);
	    	precisionTf.setEnabled(true);
	    	break;	    	
	    default: // "Integer", "Float", "Boolean", "Date", "Time", "DateTime"
	    	lengthTf.setEnabled(false);
	    	precisionTf.setEnabled(false);
	    	visibleProperty.setLength(0);
	    	visibleProperty.setPrecision(0);
	    	lengthTf.setText("0");
	    	precisionTf.setText("0");	    	
	    }    	              
    }

    @Override
    public void updateComponents() {
        super.updateComponents();
        VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
        valuesPanel.setVisibleProperty(visibleProperty);
        if(visibleProperty.getComponentType() == ComponentType.TEXT_FIELD) {
        	typeCb.setEnabled(true);
        	typeCb.setSelectedItem(visibleProperty.getDataType()); 	
        } 
        columnLabelTf.setText(visibleProperty.getColumnLabel());
        displayFormatTf.setText(visibleProperty.getDisplayFormat());
        if(visibleProperty.getEnumeration() != null) {
        	/*
        	valuesTa.setText("");
            String[] vals = visibleProperty.getEnumeration().split(";");
            for(int i=0; i<vals.length; i++) {
            	valuesTa.append(vals[i] + "\n");
            }*/
        	String[] vals = visibleProperty.getEnumeration().split(";");
        	valuesPanel.setValues(vals);
        }
        defaultValueTf.setText(visibleProperty.getDefaultValue());
        mandatoryCb.setSelected(visibleProperty.lower() != 0);
        representativeCb.setSelected(visibleProperty.isRepresentative());
        autoGoCb.setSelected(visibleProperty.isAutoGo());
        disabledCb.setSelected(visibleProperty.isDisabled());
        
        if (visibleProperty.isLabelToCode()){
        	columnLabelTf.setEditable(false);
        	labelToCodeCb.setSelected(true);
        }
        else{
        	columnLabelTf.setEditable(true);
        	labelToCodeCb.setSelected(false);
        }
       persistentTypeCb.setEnabled(true);
       if(visibleProperty.getPersistentType() == null) {
    	   persistentTypeCb.setSelectedIndex(1);
    	   visibleProperty.setPersistentType("Varchar");
       }else {
    	   System.out.println(visibleProperty.getPersistentType());
    	   persistentTypeCb.setSelectedItem(visibleProperty.getPersistentType());
       }
       updateLengthAndPrecision(visibleProperty);      
       lengthTf.setText(visibleProperty.getLength() + "");
       precisionTf.setText(visibleProperty.getPrecision() + "");
    }

    @Override
    public void updateSettings(VisibleElement visibleElement) {
        super.updateSettings(visibleElement);
        VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
        columnLabelTf.setText(visibleProperty.getColumnLabel());
        valuesPanel.setVisibleProperty(visibleProperty);
        //only show type if the component is a text field
        if(visibleElement.getComponentType() != ComponentType.TEXT_FIELD) {
        	typelbl.setVisible(false);
        	typeCb.setVisible(false);
        }else {
        	typelbl.setVisible(true);
        	typeCb.setVisible(true);
        }
        //only show values list if the component is a combo box
        if(visibleElement.getComponentType() != ComponentType.COMBO_BOX) {
        	valuesLb.setVisible(false);
        	valuesPanel.setVisible(false);
        }else {
        	valuesLb.setVisible(true);
        	valuesPanel.setVisible(true);
        }
        if(mandatoryCb.isSelected()) {
        	visibleProperty.setLower(1);
        }else {
        	visibleProperty.setLower(0);
        }
    }

    @Override
    public void updatePreformed() {
        super.updatePreformed();
    }
}
