package kroki.app.utils.uml;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.console.OutputPanel;

public class KrokiComponentOutputMessage implements IOutputMessage {

	@Override
	public void publishInfoMessage(String text) {
		KrokiMockupToolApp.getInstance().displayTextOutput(text, 0);
	}

	@Override
	public void publishErrorMessage(String text) {
		KrokiMockupToolApp.getInstance().displayTextOutput(text, 3);
	}

	@Override
	public void publishWarningMessage(String text) {
		KrokiMockupToolApp.getInstance().displayTextOutput(text, OutputPanel.KROKI_WARNING);
	}

}
