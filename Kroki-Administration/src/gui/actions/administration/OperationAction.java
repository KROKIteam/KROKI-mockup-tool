package gui.actions.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import standardForm.administration.OperationPanel;
import dao.administration.OperationHibernateDao;
import ejb.administration.Operation;
import framework.GenericStandardForm;
import framework.ReportUtil;

public class OperationAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OperationAction() {
		putValue(NAME, "Operation");
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.CTRL_MASK)); To be decided
		//putValue(SMALL_ICON, new ImageIcon("images/ukidanje_racuna.jpg"));	To be decided
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Operation operation = new Operation();
		OperationHibernateDao dao = new OperationHibernateDao();
		OperationPanel panel = new OperationPanel();
		String reportPath = ReportUtil.getReportFilePath("Operation");
		
		GenericStandardForm form = new GenericStandardForm(operation, dao , panel, reportPath);
		
		form.setModal(true);
		form.setLocationRelativeTo(null);
		form.setVisible(true);
	}
}