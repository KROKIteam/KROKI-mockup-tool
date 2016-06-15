package kroki.app.gui.console;

public class HelpText {	
	
	public String getHelpText(String command) {		
		String help = "";
		if(command.equals("help")) {
			help = "Available commands:" +
					"\n\t\t1. make project" +
					"\n\t\t2. make package" +
					"\n\t\t3. make std-panel"+
					"\n\t\t4. make parent child panel"+
					"\n\t\t5. open project"+
					"\n\t\t6. save project"+
					"\n\t\t7. undo"+
					"\n\t\t8. redo"+
					"\n\t\t9. about"+
					"\n\t\t10. clear"+
					"\n\t\t11. exit" +
					"\nFor help on specific command, type \"help command name\" (i.e. help make project)";
		}else if(command.equals("help make project")) {
			help = "\n[KROKI] make project command" +
					"\n\tSyntax: make project \"Project name\"" +
					"\n\tDescription: Creates new project with specified name in workspace." +
					"\n\tExample:" + 
					"\n\t\tmake project \"Resources\"" + 
					"\n\t\tCreates new project named \"Resources\" in workspace" +
					"\n\t\tNOTE: The project name can be written in sigle or double quotes.\n";
		}else if(command.equals("help make package")) {
			help = "\n[KROKI] make package command" +
					"\n\tSyntax: make package \"Package name\" in \"Path\"" +
					"\n\tDescription: Creates new package with specified name in specified path." +
					"\n\t\t     Path specifies hierarchial order of parent packages starting from project in workspace" +
					"\n\t\t     and ending with immediate parent package, separated by /." +
					"\n\tExample:" + 
					"\n\t\tmake package \"Workers\" in \"Resources/Human Resources\"" + 
					"\n\t\tCreates new package named \"Workers\" in package named \"Human Resources\" in project named \"Resources\"." +
					"\n\t\tNOTE: If any project or package in path is not found, it will be created." +
					"\n\t\t      The package name and path can be written in sigle or double quotes.\n";
		}else if(command.equals("help make std-panel")) {
			help = "\n[KROKI] make std-panel command" +
					"\n\tSyntax: make std-panel \"Panel name\" in \"Path\" {components}" +
					"\n\tDescription: Creates standard panel with specified name in specified path with enlisted GUI components." +
					"\n\t\t     Path specifies hierarchial order of parent packages starting from project in workspace" +
					"\n\t\t     and ending with immediate parent package, separated by /." +
					"\n\t\t     Components specify list of GUI elements to be drawn on panel, components list is enclosed in curly brackets," +
					"\n\t\t     each component in list is specified by pair type-name, separated by comma." +
					"\n\t\t     Available component types are: textfield, textarea, combobox, radiobutton, checkbox, report, transaction, link." +
					"\n\t\t     NOTE: When specifying component type, case is ignored, so textfield is same as textField or TextField." +
					"\n\tExample:" + 
					"\n\t\tmake std-panel \"Workers\" in \"Resources/Human resources\" {textfield-First name, textfield-Last name, textarea-Address, checkbox-Married}" +
					"\n\t\tNOTE: The panel name and path can be written in sigle or double quotes.\n";
		} else if (command.equals("help make parent child panel")){
			help = "\n[KROKI] make parent child panel command" +
					"\n\tSyntax: make parent-child panel \"Panel name\" in \"Path\" {components}" +
					"\n\tDescription: Creates parent child panel panel with specified name in specified path with enlisted GUI components." +
					"\n\t\t     Path specifies hierarchial order of parent packages starting from project in workspace" +
					"\n\t\t     and ending with immediate parent package, separated by /." +
					"\n\t\t     Components specify list of GUI elements to be drawn on panel, components list is enclosed in curly brackets," +
					"\n\t\t     each component in list is specified by pair type-name, separated by comma." +
					"\n\t\t     Available component types are: textfield, textarea, combobox, radiobutton, checkbox, report, transaction, link." +
					"\n\t\t     NOTE: When specifying component type, case is ignored, so textfield is same as textField or TextField." +
					"\n\tExample:" + 
					"\n\t\tmake parent child panel \"Workers\" in \"Resources/Human resources\" {textfield-First name, textfield-Last name, textarea-Address, checkbox-Married}" +
					"\n\t\tNOTE: The panel name and path can be written in sigle or double quotes.\n";
			
		} else {
			help = "No help for command \"" + command.substring(5) + "\"";
		}
		return help;
	}

}
