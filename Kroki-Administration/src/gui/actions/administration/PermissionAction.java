package gui.actions.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import dao.administration.PermissionHibernateDao;
import ejb.administration.Permission;
import framework.GenericStandardForm;
import framework.ReportUtil;
import standardForm.administration.PermissionPanel;

public class PermissionAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermissionAction() {
		putValue(NAME, "Permission");
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.CTRL_MASK)); To be decided
		//putValue(SMALL_ICON, new ImageIcon("images/ukidanje_racuna.jpg"));	To be decided
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Permission permission = new Permission();
		PermissionHibernateDao dao = new PermissionHibernateDao();
		PermissionPanel panel = new PermissionPanel();
		String reportPath = ReportUtil.getReportFilePath("Permission");
		
		GenericStandardForm form = new GenericStandardForm(permission, dao , panel, reportPath);
		
		form.setModal(true);
		form.setLocationRelativeTo(null);
		form.setVisible(true);
	}
}