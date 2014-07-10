package bp.text.box;

import java.util.HashSet;
import java.util.Set;

import bp.event.TextChangeListener;
import bp.model.data.Process;

public class RootTextBox extends CompositeTextBox {

    private Set<TextChangeListener> tcListeners;

    public RootTextBox(Process process) {
        super(null, null, null);
        appendTextBox(new ProcessTextBox(process, this));
        this.tcListeners = new HashSet<>();
        
        /* Make the association mutual */
        process.setTextBox(this);
    }

    public RootTextBox() { 
    	this.tcListeners = new HashSet<>();
    }
    
    public Set<TextChangeListener> getTcListeners() {
        return this.tcListeners;
    }

    public void setTcListeners(Set<TextChangeListener> tcListeners) {
		this.tcListeners = tcListeners;
	}

	public void addTextChangeListener(final TextChangeListener listener) {
        this.tcListeners.add(listener);
    }

    public void removeTextChangeListener(final TextChangeListener listener) {
        this.tcListeners.remove(listener);
    }

    @Override
    public void textChanged() {
        for (final TextChangeListener e : this.tcListeners) {
            e.textChanged(generateText());
        }

    }
	
}
