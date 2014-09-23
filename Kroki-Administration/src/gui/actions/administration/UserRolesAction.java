package gui.actions.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import standardForm.administration.UserRolesPanel;
import dao.administration.UserRolesHibernateDao;
import ejb.administration.UserRoles;
import framework.GenericStandardForm;
import framework.ReportUtil;

public class UserRolesAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserRolesAction() {
		putValue(NAME, "UserRoles");
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.CTRL_MASK)); To be decided
		//putValue(SMALL_ICON, new ImageIcon("images/ukidanje_racuna.jpg"));	To be decided
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		UserRoles userroles = new UserRoles();
		UserRolesHibernateDao dao = new UserRolesHibernateDao();
		UserRolesPanel panel = new UserRolesPanel();
		String reportPath = ReportUtil.getReportFilePath("UserRoles");
		
		GenericStandardForm form = new GenericStandardForm(userroles, dao , panel, reportPath);
		
		form.setModal(true);
		form.setLocationRelativeTo(null);
		form.setVisible(true);
	}
}