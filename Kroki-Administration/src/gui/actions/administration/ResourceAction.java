package gui.actions.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import standardForm.administration.ResourcePanel;
import dao.administration.ResourceHibernateDao;
import ejb.administration.Resource;
import framework.GenericStandardForm;
import framework.ReportUtil;

public class ResourceAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceAction() {
		putValue(NAME, "Resource");
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.CTRL_MASK)); To be decided
		//putValue(SMALL_ICON, new ImageIcon("images/ukidanje_racuna.jpg"));	To be decided
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Resource resource = new Resource();
		ResourceHibernateDao dao = new ResourceHibernateDao();
		ResourcePanel panel = new ResourcePanel();
		String reportPath = ReportUtil.getReportFilePath("Resource");
		
		GenericStandardForm form = new GenericStandardForm(resource, dao , panel, reportPath);
		
		form.setModal(true);
		form.setLocationRelativeTo(null);
		form.setVisible(true);
	}
}