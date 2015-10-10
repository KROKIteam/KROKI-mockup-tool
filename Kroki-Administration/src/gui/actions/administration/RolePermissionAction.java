package gui.actions.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import standardForm.administration.RolePermissionPanel;
import dao.administration.RolePermissionHibernateDao;
import ejb.administration.RolePermission;
import framework.GenericStandardForm;
import framework.ReportUtil;

public class RolePermissionAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RolePermissionAction() {
		putValue(NAME, "RolePermission");
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.CTRL_MASK)); To be decided
		//putValue(SMALL_ICON, new ImageIcon("images/ukidanje_racuna.jpg"));	To be decided
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		RolePermission rolepermission = new RolePermission();
		RolePermissionHibernateDao dao = new RolePermissionHibernateDao();
		RolePermissionPanel panel = new RolePermissionPanel();
		String reportPath = ReportUtil.getReportFilePath("RolePermission");
		
		GenericStandardForm form = new GenericStandardForm(rolepermission, dao , panel, reportPath);
		
		form.setModal(true);
		form.setLocationRelativeTo(null);
		form.setVisible(true);
	}
}