package bp.text.box;

import bp.event.AttributeChangeListener;
import bp.model.data.Activity;
import bp.model.data.ActivityEvent;
import bp.model.data.ConditionalActivityEvent;
import bp.model.data.Edge;
import bp.model.data.Element;
import bp.model.data.ErrorActivityEvent;
import bp.model.data.MessageActivityEvent;
import bp.model.data.Process;
import bp.model.data.SignalActivityEvent;
import bp.model.data.Task;
import bp.model.data.TimerActivityEvent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class ProcessTextBox extends BlockTextBox {

    private Process process;

    public ProcessTextBox(final Process process, final TextBox owner) {
        super(BPKeyWords.PROCESS, process.getUniqueName(), owner);
        this.process = process;
        addDataListener();
        addElementsListener();
        setIndentationLevel(0);
    }

    public ProcessTextBox() { }
    
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        if (value != null) {
            if (keyWord == BPKeyWords.UNIQUE_NAME) {
                setValue(value);
                textChanged();
            } else if (keyWord == BPKeyWords.NAME || keyWord == BPKeyWords.DESCRIPTION || keyWord == BPKeyWords.DATA) {
                updateAttribute(keyWord, value);
            }
        }
    }

    protected void updateAttribute(final BPKeyWords keyWord, final Object value) {
        final AttributeTextBox attTextBox = getAttributeTextBox(keyWord);
        if (attTextBox != null) {
            attTextBox.setValue(value);
            textChanged();
        } else {
            appendTextBox(new AttributeTextBox(keyWord, value, this));
            textChanged();
        }
    }

    private void addDataListener() {
        this.process.addAttributeChangeListener(new AttributeChangeListener() {

            @Override
            public Controller getController() {
                return Controller.TEXT;
            }

            @Override
            public void fireAttributeChanged(final BPKeyWords keyWord, final Object value) {
                dataAttributeChanged(keyWord, value);
            }
        });
    }

    private void addElementsListener() {
        this.process.addElementsListener(new ElementsListener());
    }

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}
	
	public class ElementsListener extends bp.event.ElementsListener {
		
		public ElementsListener() { }
		
		@Override
        public void elementRemoved(final Element e) {
            getTextBoxes().remove(e);
            textChanged();
        }

        @Override
        public void elementAdded(final Element e) {
            if (!getTextBoxes().contains(e)) {
                if (e instanceof Task) {
                    appendTextBox(new TaskTextBox((Task) e, BPKeyWords.TASK, ProcessTextBox.this));
                    textChanged();
                } else if (e instanceof Edge) {
                    appendTextBox(new EdgeTextBox((Edge) e, BPKeyWords.EDGE, ProcessTextBox.this));
                    textChanged();
                } else if (e instanceof ActivityEvent) {
                    final ActivityEvent aEvent = (ActivityEvent) e;
                    final Activity a = aEvent.getActivity();
                    for (final TextBox tb : getTextBoxes()) {
                        if (tb instanceof TaskTextBox) {
                            final TaskTextBox ttb = (TaskTextBox) tb;
                            if (ttb.getTask().equals(a)) {
                                if (aEvent instanceof MessageActivityEvent) {
                                    ttb.appendTextBox(new ActivityEventTextBox(aEvent,
                                            BPKeyWords.MESSAGE_ACTIVITY_EVENT, ttb));
                                } else if (aEvent instanceof ConditionalActivityEvent) {
                                    ttb.appendTextBox(new ActivityEventTextBox(aEvent,
                                            BPKeyWords.CONDITIONAL_ACTIVITY_EVENT, ttb));
                                } else if (aEvent instanceof TimerActivityEvent) {
                                    ttb.appendTextBox(new ActivityEventTextBox(aEvent,
                                            BPKeyWords.TIMER_ACTIVITY_EVENT, ttb));
                                } else if (aEvent instanceof SignalActivityEvent) {
                                    ttb.appendTextBox(new ActivityEventTextBox(aEvent,
                                            BPKeyWords.SIGNAL_ACTIVITY_EVENT, ttb));
                                } else if (aEvent instanceof ErrorActivityEvent) {
                                    ttb.appendTextBox(new ActivityEventTextBox(aEvent,
                                            BPKeyWords.ERROR_ACTIVITY_EVENT, ttb));
                                }
                                textChanged();
                            }
                        }
                    }
                }

            }
        }
	}


}
