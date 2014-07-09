package com.panelcomposer.elements.spanel;

import java.awt.CardLayout;
import java.awt.color.ColorSpace;
import java.awt.image.ColorConvertOp;
import java.lang.reflect.Method;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;

import util.EntityHelper;
import util.resolvers.ComponentResolver;
import util.staticnames.Messages;

import com.panelcomposer.converters.ConverterUtil;
import com.panelcomposer.elements.SForm;
import com.panelcomposer.elements.STModel;
import com.panelcomposer.elements.STable;
import com.panelcomposer.elements.SToolBar;
import com.panelcomposer.enumerations.OpenedAs;
import com.panelcomposer.enumerations.StateMode;
import com.panelcomposer.enumerations.ViewMode;
import com.panelcomposer.exceptions.EntityAttributeNotFoundException;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;
import com.panelcomposer.model.panel.MPanel;
import com.panelcomposer.model.panel.MStandardPanel;
import com.panelcomposer.observe.ObserverPanel;

@SuppressWarnings("serial")
public class SPanel extends JPanel implements ObserverPanel {

	// S(tandard) elements
	private STable table;
	private SToolBar toolbar;
	private SForm form;
	private SPanel ownerPanel;
	// specific panels
	private InputPanel inputPanel;
	private JPanel cardPanel = new JPanel();
	private CardLayout cardLayout = new CardLayout();
	private LockableUI lockableUI = new LockableUI();

	/***
	 * Model for standard panel
	 */
	protected MStandardPanel modelPanel;

	public SPanel(MPanel mpanel, SForm form) {
		super();
		this.form = form;
		this.modelPanel = (MStandardPanel) mpanel;
		init();
	}

	public SPanel(MPanel mpanel, SForm form, SPanel ownerPanel,
			OpenedAs openedAs, String dataFilter) {
		super();
		this.form = form;
		this.modelPanel = (MStandardPanel) mpanel;
		this.ownerPanel = ownerPanel;
		this.modelPanel.getDataSettings().setDataFilter(dataFilter);
		this.modelPanel.getPanelSettings().setOpenedAs(openedAs);
		if (openedAs == OpenedAs.ZOOM) {
			modelPanel.getPanelSettings().setViewMode(ViewMode.TABLEVIEW);
		}
		init();
	}

	public void init() {
		getModelPanel().getPanelSettings().setStateMode(StateMode.ADD);
		setLayout(new MigLayout("", "[0:0,grow 100,fill][pref!]", "[]0[]"));

		JPanel panelContainer = new JPanel();
		panelContainer.setLayout(new MigLayout());

		cardPanel = new JPanel(cardLayout);
		addToolBar();
		addPanelAndTable();
		cardPanel.setPreferredSize(getTable().getScrollPane()
				.getPreferredSize());

		OperationsPanel op = new OperationsPanel(this);
		if (modelPanel.getStandardOperations().hasAllowedOperations()) {
			panelContainer.add(cardPanel, "id cardPanel, pos 0 0 80% 100%");
			panelContainer.add(op, "pos (cardPanel.x2) 0 100% 100%");
		} else {
			panelContainer.add(cardPanel, "id cardPanel, pos 0 0 100% 100%");
		}

		ColorConvertOp grayScale = new ColorConvertOp(
				ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		BufferedImageOpEffect effect = new BufferedImageOpEffect(grayScale);
		JXLayer<JComponent> layer = new JXLayer<JComponent>(panelContainer,
				lockableUI);
		lockableUI.setLockedEffects(effect);
		lockableUI.setLocked(false);
		add(layer, "span, wrap");

		loadData(null);
	}

	public void handleAdd() {
		getModelPanel().getPanelSettings().setStateMode(StateMode.ADD);
		getInputPanel().clearComponentFields();
	}

	public void handleCancel() {
		getModelPanel().getPanelSettings().setStateMode(StateMode.UPDATE);
		getTable().requestFocus();
		refreshInputPanel();
	}

	public void handleCommit() {
		System.out.println("HANDLE COMMIT");
		if (!getModelPanel().getPanelSettings().getStateMode().equals(StateMode.SEARCH)) {
			// TODO: Needs validation
			// return;
			Object objEntity = null;
			try {
				if(getModelPanel().getPanelSettings().getStateMode().equals(StateMode.ADD)) {
					System.out.println("ADD");
					objEntity = ComponentResolver.getDataFromComponents(
							getTable().getTableModel().getEntityBean(),
							getInputPanel().getPanelComponents(), null);
					getTable().getTableModel().add(objEntity);
					getModelPanel().getPanelSettings().setStateMode(StateMode.ADD);
				} else if(getModelPanel().getPanelSettings().getStateMode().equals(StateMode.UPDATE)) {
					if(getModelPanel().getPanelSettings().getStateMode().equals(StateMode.UPDATE)) {
						Object o = ((STModel) getTable().getModel()).getObject(getTable().getSelectedRow());
						Method method = o.getClass().getMethod("getId");
						Long id = (Long) method.invoke(o);
						objEntity = ComponentResolver.getDataFromComponents(
								getTable().getTableModel().getEntityBean(),
								getInputPanel().getPanelComponents(), o);
						method = objEntity.getClass().getMethod("setId", Long.class);
						method.invoke(objEntity, id);
					}
					getTable().getTableModel().update(objEntity);

				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(getForm(), 
						Messages.INCORRECT_DATA_INPUT, Messages.ERROR, JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void handleStartSearch() {
		if (getModelPanel().getPanelSettings().getStateMode() == StateMode.SEARCH) {
			String whereStatement = "";
			String value = "";
			JComponent component = null;
			ColumnAttribute ca = null;
			JoinColumnAttribute jca = null;
			EntityBean bean = getModelPanel().getEntityBean();
			int index = -1;
			for (int i = 0; i < bean.getAttributes().size(); i++) {
				ca = null;
				jca = null;
				try {
					if (bean.getAttributes().get(i) instanceof ColumnAttribute) {
						ca = (ColumnAttribute) bean.getAttributes().get(i);
						index = EntityHelper.getIndexOf(bean, ca);
					} else if (bean.getAttributes().get(i) instanceof JoinColumnAttribute) {
						jca = (JoinColumnAttribute) bean.getAttributes().get(i);
						ca = jca.getZoomedByAsColumn();
						index = EntityHelper.getIndexOfForJoin(bean, ca, jca);
					}
					component = getInputPanel().getPanelComponents().get(index);
					if (component instanceof JTextField) {
						value = ((JTextField) component).getText().trim();
					} else if (component instanceof JComboBox) {
						int sel = ((JComboBox) component).getSelectedIndex();
						value = Integer.toString(sel);
					}
					if (value != null && !value.equals("")) {
						value = ConverterUtil.convertForSQL(value, ca);
						whereStatement += "x.";
						if (jca != null) {
							whereStatement += jca.getFieldName() + ".";
						}
						whereStatement += ca.getFieldName();
						if (value.contains("*") || value.contains("?")) {
							value = value.replace("*", "%").replace("?", "_");
							whereStatement += " LIKE '" + value + "' AND ";
						} else {
							whereStatement += " = '" + value + "' AND ";
						}
						System.out.println(whereStatement);
					}
				} catch (EntityAttributeNotFoundException e) {
					e.printStackTrace();
				}
			}

			if (!whereStatement.equals("")) {
				whereStatement = " WHERE "
						+ whereStatement.substring(0,
								whereStatement.length() - 4);
				loadData(whereStatement);
				refreshInputPanel();
			} else {
				JOptionPane.showMessageDialog(getForm(),
						Messages.SEARCH_NO_FIELDS_FILLED,
						Messages.SEARCH_TITLE, JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public void handleSearch() {
		getModelPanel().getPanelSettings().setStateMode(StateMode.SEARCH);
		inputPanel.clearComponentFields();
		getInputPanel().getBtnStartSearch().setEnabled(true);
	}

	public void handleCopy() {
		// TODO: To implement

	}

	public void loadData(String where) {
		try {
			if (where == null) {
				String filter = getModelPanel().getDataSettings()
						.getDataFilter();
				if (filter == null) {
					filter = "";
				}
				table.getTableModel().refreshData(filter);
			} else {
				table.getTableModel().refreshData(where);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.getForm(),
					"Nema podataka za prikazati.");
		}
	}

	public void refreshInputPanel() {
		int rowIndex = getTable().getSelectedRow();
		if (rowIndex != -1) {
			table.getTableModel().setCurrentRow(rowIndex);
			EntityBean ejb = table.getTableModel().getEntityBean();
			ComponentResolver.fillPanel(ejb, this, rowIndex);
		} else {
			inputPanel.clearComponentFields();
		}
	}

	public void dispose() {
		table.getTableModel().dispose();
		form.dispose();
	}

	public STable getTable() {
		return table;
	}

	public void setTable(STable table) {
		this.table = table;
	}

	public SToolBar getToolbar() {
		return toolbar;
	}

	public void setToolbar(SToolBar toolbar) {
		this.toolbar = toolbar;
	}

	public MStandardPanel getModelPanel() {
		return modelPanel;
	}

	public void setModelPanel(MStandardPanel modelPanel) {
		this.modelPanel = modelPanel;
	}

	public SForm getForm() {
		return form;
	}

	public void setForm(SForm form) {
		this.form = form;
	}

	public InputPanel getInputPanel() {
		return inputPanel;
	}

	public void setInputPanel(InputPanel inputPanel) {
		this.inputPanel = inputPanel;
	}

	public JPanel getCardPanel() {
		return cardPanel;
	}

	public void setCardPanel(JPanel cardPanel) {
		this.cardPanel = cardPanel;
	}

	public CardLayout getCardLayout() {
		return cardLayout;
	}

	public void setCardLayout(CardLayout cardLayout) {
		this.cardLayout = cardLayout;
	}

	public LockableUI getLockableUI() {
		return lockableUI;
	}

	public void setLockableUI(LockableUI lockableUI) {
		this.lockableUI = lockableUI;
	}

	public SPanel getOwnerPanel() {
		return ownerPanel;
	}

	public void setOwnerPanel(SPanel ownerPanel) {
		this.ownerPanel = ownerPanel;
	}

}
