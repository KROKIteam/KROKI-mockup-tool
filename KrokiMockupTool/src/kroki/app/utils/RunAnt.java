package kroki.app.utils;

import java.io.File;

import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.omg.CORBA.PUBLIC_MEMBER;

public class RunAnt {
	
	public void runBuild(String jarName, File antFile, File outputFile) {
		System.out.println("RUN ANT jarName: " + jarName + ", antFile: " + antFile.getAbsolutePath() + ", outputFile: " + outputFile.getAbsolutePath() );
		Project p = new Project();
		p.setProperty("deploy.home", outputFile.getAbsolutePath());
		p.setProperty("jar.name", jarName);
		p.setUserProperty("ant.file", antFile.getAbsolutePath());
		p.init();
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		p.addReference("ant.projectHelper", helper);
		helper.parse(p, antFile);
		p.executeTarget(p.getDefaultTarget());
	}

	public void runRun(String jarName, File jarDir, boolean swing) {
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		
		File buildFile = new File(appPath.substring(0, appPath.length()-16) + "SwingApp" + File.separator + "run.xml");
		
		if(!swing) {
			buildFile = new File(appPath.substring(0, appPath.length()-16) + "WebApp" + File.separator + "run.xml");
		}
		
		System.out.println("ANT RUNNER: \njarName: " + jarName + ".jar" + "\nDir: " + jarDir.getAbsolutePath()+ "\\" + jarName + "\nBuildFile: " + buildFile.getAbsolutePath());
		
		Project p = new Project();
		p.setProperty("jar.dir", jarDir.getAbsolutePath() + File.separator + jarName);
		p.setProperty("jar.name", jarName);
		p.setUserProperty("ant.file", buildFile.getAbsolutePath());
		p.init();
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		p.addReference("ant.projectHelper", helper);
		helper.parse(p, buildFile);
		p.executeTarget(p.getDefaultTarget());
	}
	
}
