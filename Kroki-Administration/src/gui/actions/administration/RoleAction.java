package gui.actions.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import dao.administration.RoleHibernateDao;
import ejb.administration.Role;
import framework.GenericStandardForm;
import framework.ReportUtil;
import standardForm.administration.RolePanel;

public class RoleAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoleAction() {
		putValue(NAME, "Role");
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.CTRL_MASK)); To be decided
		//putValue(SMALL_ICON, new ImageIcon("images/ukidanje_racuna.jpg"));	To be decided
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Role role = new Role();
		RoleHibernateDao dao = new RoleHibernateDao();
		RolePanel panel = new RolePanel();
		String reportPath = ReportUtil.getReportFilePath("Role");
		
		GenericStandardForm form = new GenericStandardForm(role, dao , panel, reportPath);
		
		form.setModal(true);
		form.setLocationRelativeTo(null);
		form.setVisible(true);
	}
}