package gui.actions.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import dao.administration.ResourceHierarchyHibernateDao;
import ejb.administration.ResourceHierarchy;
import framework.GenericStandardForm;
import framework.ReportUtil;
import standardForm.administration.ResourceHierarchyPanel;

public class ResourceHierarchyAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceHierarchyAction() {
		putValue(NAME, "ResourceHierarchy");
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.CTRL_MASK)); To be decided
		//putValue(SMALL_ICON, new ImageIcon("images/ukidanje_racuna.jpg"));	To be decided
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ResourceHierarchy resourcehierarchy = new ResourceHierarchy();
		ResourceHierarchyHibernateDao dao = new ResourceHierarchyHibernateDao();
		ResourceHierarchyPanel panel = new ResourceHierarchyPanel();
		String reportPath = ReportUtil.getReportFilePath("ResourceHierarchy");
		
		GenericStandardForm form = new GenericStandardForm(resourcehierarchy, dao , panel, reportPath);
		
		form.setModal(true);
		form.setLocationRelativeTo(null);
		form.setVisible(true);
	}
}