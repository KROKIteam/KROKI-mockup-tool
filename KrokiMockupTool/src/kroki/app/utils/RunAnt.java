package kroki.app.utils;

import java.io.File;

import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.omg.CORBA.PUBLIC_MEMBER;

public class RunAnt {
	
	public void run(String jarName, File antFile, File outputFile) {
		System.out.println("RUN ANT jarName: " + jarName + ", antFile: " + antFile.getAbsolutePath() + ", outputFile: " + outputFile.getAbsolutePath() );
//		JOptionPane.showMessageDialog(null, "RUN ANT jarName: " + jarName + ", antFile: " + antFile.getAbsolutePath() + ", outputFile: " + outputFile.getAbsolutePath());
		Project p = new Project();
		p.setProperty("deploy.home", outputFile.getAbsolutePath());
		p.setProperty("jar.name", jarName);
		p.setUserProperty("ant.file", antFile.getAbsolutePath());
		p.init();
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		p.addReference("ant.projectHelper", helper);
		helper.parse(p, antFile);
		p.executeTarget(p.getDefaultTarget());
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("[KROKI] project exported to " + outputFile.getAbsolutePath());
	}

}
