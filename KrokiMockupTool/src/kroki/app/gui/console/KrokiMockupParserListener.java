package kroki.app.gui.console;

import kroki.app.gui.console.KrokiMockupToolCommandSyntaxParser.MakeComponentContext;
import kroki.app.gui.console.KrokiMockupToolCommandSyntaxParser.MakePackageContext;
import kroki.app.gui.console.KrokiMockupToolCommandSyntaxParser.MakeProjectContext;
import kroki.app.gui.console.KrokiMockupToolCommandSyntaxParser.MakeStdPanelContext;


public class KrokiMockupParserListener extends KrokiMockupToolCommandSyntaxBaseListener {
	
	public void exitMakeProject(MakeProjectContext ctx) {
		CommandPanel.makeProjectCommand(ctx.getText());
	}
	
	public void exitMakePackage(MakePackageContext ctx){
		CommandPanel.makePackageCommand(ctx.getText());
	}
	
	public void exitMakeStdPanel(MakeStdPanelContext ctx) {
		CommandPanel.makeStdPanelCommand(ctx.getText());
	}
	
	public void exitMakeComponent(MakeComponentContext ctx) {
		CommandPanel.addComponentToPanel(ctx.getText());
	}
}