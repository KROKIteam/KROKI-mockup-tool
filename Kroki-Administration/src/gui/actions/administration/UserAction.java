package gui.actions.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import standardForm.administration.UserPanel;
import dao.administration.UserHibernateDao;
import ejb.administration.User;
import framework.GenericStandardForm;
import framework.ReportUtil;

public class UserAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAction() {
		putValue(NAME, "User");
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.CTRL_MASK)); To be decided
		//putValue(SMALL_ICON, new ImageIcon("images/ukidanje_racuna.jpg"));	To be decided
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		User user = new User();
		UserHibernateDao dao = new UserHibernateDao();
		UserPanel panel = new UserPanel();
		String reportPath = ReportUtil.getReportFilePath("User");
		
		GenericStandardForm form = new GenericStandardForm(user, dao , panel, reportPath);
		
		form.setModal(true);
		form.setLocationRelativeTo(null);
		form.setVisible(true);
	}
}