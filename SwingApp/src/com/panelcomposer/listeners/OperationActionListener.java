package com.panelcomposer.listeners;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import util.EntityHelper;
import util.resolvers.ComponentResolver;
import util.staticnames.Messages;
import util.staticnames.Settings;

import com.panelcomposer.business.procedure.ProcedureParameter;
import com.panelcomposer.business.procedure.StoredProcedure;
import com.panelcomposer.business.report.AbstractReportView;
import com.panelcomposer.business.report.jasper.JasperReportView;
import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.exceptions.StoredProcedureException;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;
import com.panelcomposer.model.panel.configuration.operation.Operation;
import com.panelcomposer.model.panel.configuration.operation.Parameter;
import com.panelcomposer.model.panel.configuration.operation.ParameterType;

public class OperationActionListener implements ActionListener {

	protected Operation operation;
	protected SPanel panel;
	protected JDialog dialog;
	protected List<JComponent> components;
	
	public OperationActionListener(Operation operation, SPanel panel) {
		this.operation = operation;
		this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		System.out.println("OperationActionListener.actionPerformed()");
		
		components = new ArrayList<JComponent>();
		int count = operation.getParameterCount() + 1;
		
		System.out.println("count: " + count);
		
		dialog = new JDialog(panel.getForm(), true);
		dialog.setPreferredSize(new java.awt.Dimension(300, 50 + count * 40));
		dialog.setLayout(new MigLayout("", "[0:0,grow 100,fill]", ""));
		dialog.setTitle(operation.getLabel());
		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new GridLayout(count, 2));

		int row = panel.getTable().getSelectedRow();
		EntityBean bean = panel.getModelPanel().getEntityBean();
		ColumnAttribute ca = null; 
		for (Parameter param : operation.getParameters()) {
			
			try {
				String label = param.getLabel() + ":";
				dialogPanel.add(new JLabel(label));
				JComponent component = null;
				String type = param.getType().getName();
				component = ComponentResolver.getComponentForType(type); 
				component.setEnabled(false);
				String name = param.getName();
				if(!param.getParameterType().equals(ParameterType.OUT)) {
					try {
						ca = (ColumnAttribute) EntityHelper.getAttribute(bean, name);
						int col = EntityHelper.getIndexOf(bean, ca);
						Object value = panel.getTable().getTableModel().getValueAt(row, col);
						param.setValue(value);
						System.out.println("param value = " + param.getValue()); 
						component = ComponentResolver.setValue(component, value, ca);
					} catch (Exception e1) {
						e1.printStackTrace();
						Object value = param.getDefaultValue();
						System.out.println("OperationActionListener.actionPerformed() value="+value);
						if(value != null) 
							((JTextField) component).setText(value.toString());
					}
				}
				components.add(component);
				dialogPanel.add(component);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(dialog, "Greska pri kreiranju dijaloga za unos/potvrdu parametara!", 
						Messages.ERROR, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		JButton executeButton = new JButton(Settings.BTN_EXECUTE);
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					switch(operation.getType()) {
						case VIEWREPORT :	
							callViewReport(operation);							
							break;
						case BUSSINESTRANSACTION : 
							callTransaction(operation);
							break;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(dialog, "Neispravno uneti podaci!", Messages.ERROR, JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		dialog.getRootPane().setDefaultButton(executeButton);
		dialogPanel.add(executeButton);
		
		JButton cancelButton = new JButton(Settings.BTN_CANCEL);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		dialogPanel.add(cancelButton);
		dialog.add(dialogPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(panel.getForm());
		dialog.setVisible(true);
	
	}
	
	/***
	 * 
	 * @param operation
	 */
	protected void callViewReport(Operation operation) {
		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			for(Parameter param : operation.getParameters()) {
				parameterMap.put(param.getName(), param.getValue());
			}
			AbstractReportView arw = new JasperReportView(
					parameterMap, operation.getTarget());
			panel.getForm().setModal(false);
			dialog.dispose();
			arw.execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(dialog, 
					"Nije nadjen izvestaj", Messages.ERROR, JOptionPane.ERROR_MESSAGE);
		} finally {
			panel.loadData(null);
		}
	}

	/***
	 * 
	 * @param operation
	 * @return
	 */
	private List<Object> callTransaction(Operation operation) {
		String procedureName = operation.getTarget();
		List<ProcedureParameter> parameters = new ArrayList<ProcedureParameter>();
		ProcedureParameter procParam = null;
		for(Parameter param : operation.getParameters()){
			procParam = new ProcedureParameter();
			procParam.setType(param.getParameterType());
			Object value = param.getDefaultValue();
			if(procParam.getType().equals(ParameterType.IN) ||
					procParam.getType().equals(ParameterType.INOUT)) {
				procParam.setValue(value);
			} else if(procParam.getType().equals(ParameterType.OUT)) {
				procParam.setValue(param.getType());
			}
			parameters.add(procParam);
		}
		StoredProcedure ap = new StoredProcedure(procedureName, parameters);
		List<Object> output = null;
		try {
			output = ap.execute();
			Parameter param = null;
			int counter = 0;
			for (int i = 0; i < operation.getParameters().size(); i++) {
				param = operation.getParameters().get(i);
				if(param.getParameterType().equals(ParameterType.OUT) ||
						param.getParameterType().equals(ParameterType.INOUT)) {
					((JTextField) components.get(i)).setText(output.get(counter).toString());
					counter++;
				}
			}
			return output;
		} catch (StoredProcedureException e) {
			JOptionPane.showMessageDialog(dialog, e.getMessage(), Messages.ERROR, JOptionPane.ERROR_MESSAGE);
		}
		return output;
	}

}
