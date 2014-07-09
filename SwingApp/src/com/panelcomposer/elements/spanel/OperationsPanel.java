package com.panelcomposer.elements.spanel;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.panelcomposer.listeners.OperationActionListener;
import com.panelcomposer.model.panel.configuration.operation.Operation;

@SuppressWarnings("serial")
public class OperationsPanel extends JPanel {

	protected SPanel panel;

	public OperationsPanel(SPanel panel) {
		super();
		this.panel = panel;
		init();
	}

	private void init() {
		MigLayout layout = new MigLayout("", "[grow 100,fill]", "[]rel[]");
		setLayout(layout);
		setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.GRAY));
		List<Operation> operations = panel.getModelPanel()
				.getStandardOperations().getOperations();
		
		for(Operation op : operations) {
			if(op.getAllowed() == true) {
				JButton btn = new JButton(op.getLabel());
				btn.setName(op.getName());
				btn.addActionListener(new OperationActionListener(op, panel));
				add(btn, "span, wrap");
			}
		}
	}

}
