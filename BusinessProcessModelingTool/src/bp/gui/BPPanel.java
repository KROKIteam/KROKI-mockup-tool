package bp.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import bp.app.AppCore;
import bp.model.data.Process;
import bp.state.StateManager;
import bp.text.box.RootTextBox;

public class BPPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1788034027677048992L;

    private BPGraphicPanel graphicsPanel;
    private JScrollPane graphicsScroll;
    private BPTextPanel text;
    private JScrollPane textScroll;
    private JSplitPane splitter;

    private RootTextBox textBox;
    private Process process;
    private StateManager stateManager;

    public BPPanel() { }
    
    public BPPanel(String uniqueName) {
        this.process = new Process(uniqueName);
        AppCore.getInstance().updateDetails(this.process.getDetails());

        this.textBox = new RootTextBox(this.process);
        this.text = new BPTextPanel(this.textBox);

        this.graphicsPanel = new BPGraphicPanel(this);
        this.graphicsScroll = new JScrollPane(this.graphicsPanel);
        this.textScroll = new JScrollPane(this.text);
        this.splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        this.stateManager = new StateManager(this);

        initializeView();
    }
    
    public BPPanel(Process process) {
    	this.process = process;
    	
    	AppCore.getInstance().updateDetails(this.process.getDetails());

        //this.textBox = new RootTextBox(this.process);
    	this.textBox = process.getTextBox();
    	
        this.text = new BPTextPanel(this.textBox);

        this.graphicsPanel = new BPGraphicPanel(this);
        this.graphicsScroll = new JScrollPane(this.graphicsPanel);
        this.textScroll = new JScrollPane(this.text);
        this.splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        this.stateManager = new StateManager(this);

        initializeView();
    	
    }

    private void initializeView() {
        setLayout(new BorderLayout());

        this.splitter.add(this.graphicsScroll);
        this.splitter.add(this.textScroll);
        this.splitter.setDividerLocation(500);

        add(this.splitter, BorderLayout.CENTER);

        validate();
    }

    public BPGraphicPanel getGraphicsPanel() {
        return this.graphicsPanel;
    }

    public BPTextPanel getText() {
        return this.text;
    }

    public Process getProcess() {
        return this.process;
    }
    
    public JScrollPane getGraphicsScroll() {
		return graphicsScroll;
	}

	public JScrollPane getTextScroll() {
		return textScroll;
	}

	public JSplitPane getSplitter() {
		return splitter;
	}

	public RootTextBox getTextBox() {
		return textBox;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public StateManager getStateManager() {
        return this.stateManager;
    }

	public void setGraphicsPanel(BPGraphicPanel graphicsPanel) {
		this.graphicsPanel = graphicsPanel;
	}

	public void setGraphicsScroll(JScrollPane graphicsScroll) {
		this.graphicsScroll = graphicsScroll;
	}

	public void setText(BPTextPanel text) {
		this.text = text;
	}

	public void setTextScroll(JScrollPane textScroll) {
		this.textScroll = textScroll;
	}

	public void setSplitter(JSplitPane splitter) {
		this.splitter = splitter;
	}

	public void setTextBox(RootTextBox textBox) {
		this.textBox = textBox;
	}

	public void setStateManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}

}
