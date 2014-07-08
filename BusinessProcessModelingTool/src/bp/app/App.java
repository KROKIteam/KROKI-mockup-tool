package bp.app;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class App {

    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        AppCore appCore = AppCore.getInstance();
        
        appCore.setProjectFile(new File("C:/Users/specijalac/Documents/Kroki/Kroki.kroki"));
        System.out.println("Enclosing project file path: " + appCore.getProjectFile().getAbsolutePath());
    	
        //appCore.createBPPanel();
        appCore.loadProcess(bp.util.WorkspaceUtility.load(new File("C:/Users/specijalac/Documents/Kroki/test289.kroki_bpm")));

        appCore.setRunningStandalone(true);
    	appCore.setVisible(true);
    }
}
