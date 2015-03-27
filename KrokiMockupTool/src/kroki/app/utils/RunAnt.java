package kroki.app.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.profil.subsystem.BussinesSubsystem;

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

	public void runRun(BussinesSubsystem proj, File jarDir, boolean swing) {
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyHmm");
		Date today = new Date();
		String dateSuffix = formatter.format(today);
		String jarName = proj.getLabel().replace(" ", "_") + "_WEB_" + formatter.format(today);
		
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		if(!KrokiMockupToolApp.getInstance().isBinaryRun()) {
			appPath = appPath.substring(0, appPath.length()-16);
		}
		
		File buildFile = new File(appPath + "SwingApp" + File.separator + "run.xml");
		
		// If the project has associated Eclipse project, run it's run.xml instead of the default
		if(proj.getEclipseProjectPath() != null) {
			if(!swing) {
				buildFile = new File(proj.getEclipseProjectPath().getAbsolutePath() + File.separator + "WebApp" + File.separator + "run.xml");
			}
		}else {
			if(!swing) {
				buildFile = new File(appPath + "WebApp" + File.separator + "run.xml");
			}
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
