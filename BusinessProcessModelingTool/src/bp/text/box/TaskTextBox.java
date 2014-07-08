package bp.text.box;

import bp.model.data.Task;
import bp.model.util.BPKeyWords;

public class TaskTextBox extends ActivityTextBox {

    public TaskTextBox(final Task task, final BPKeyWords keyWord, final TextBox owner) {
        super(task, keyWord, owner);
    }

    public TaskTextBox() { }
    
    @Override
    protected void dataAttributeChanged(final BPKeyWords keyWord, final Object value) {
        super.dataAttributeChanged(keyWord, value);
        if (value != null) {
            if (keyWord == BPKeyWords.ACTOR || keyWord == BPKeyWords.AUTO_ASSIGN
                    || keyWord == BPKeyWords.MULTIPLE_EXECUTION || keyWord == BPKeyWords.MULTIPLE_EXECUTION_TYPE) {
                updateAttribute(keyWord, value);
            }

            // TODO: lane actor
        }
    }

    public Task getTask() {
        return (Task) getElement();
    }

}
