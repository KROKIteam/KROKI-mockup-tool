package framework;

import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.AbstractAction;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;

@SuppressWarnings("serial")
public class ReportAction extends AbstractAction {
	
	private String path;

	public ReportAction(String path) {
		super();
		this.path = path;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		try{
			Transaction transaction = HibernateUtil.getSessionfactory().getCurrentSession().beginTransaction();
			HibernateUtil.getSessionfactory().getCurrentSession().doWork(new Work() {
				
				@Override
				public void execute(Connection connection) throws SQLException {
					JasperPrint jp;
					try {
						jp = JasperFillManager.fillReport(
								getClass().getResource(path).openStream()
								, null
								, connection);
						JasperViewer viewer = new JasperViewer(jp, false);
						viewer.setAlwaysOnTop(true);
                        viewer.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
                        viewer.setVisible(true);
                        viewer.setAlwaysOnTop(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			transaction.commit();
			} catch (Exception exc){
				exc.printStackTrace();
			}
	}

}
