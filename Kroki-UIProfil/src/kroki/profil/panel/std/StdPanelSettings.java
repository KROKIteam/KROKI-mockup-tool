package kroki.profil.panel.std;

import java.io.Serializable;
import kroki.profil.panel.mode.OperationMode;
import kroki.profil.panel.mode.ViewMode;

/**
 * <code>StdPanelSettings</code> defines a set of standard panel settings
 * which specify its behavior
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class StdPanelSettings implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**Default view mode - view mode in which the panel is first shown. Possible values
	 * are defined with ViewMode enumerated type*/
    private ViewMode defaultViewMode = ViewMode.INPUT_PANEL_MODE;
    /**Default operation mode - operation mode in which the panel is first shown*/
    private OperationMode defaultOperationMode = OperationMode.VIEW_MODE;
    /**Indicates if the user should confirm deletion of a row or not*/
    private boolean confirmDelete = false;
    /**Indicates if the panel stays in the add mode after one row has been entered or 
     * returns to the view mode (which is common in cases when a document with header and 
     * items is being modeled. Once a header is created (one row), one usually 
     * continues by entering items.*/
    private boolean stayInAddMode = false;
    /**Indicates if the last entered row should be selected after leaving the add mode.
     * Alternatively, the first row is selected*/
    private boolean goToLastAdded = true;

    public StdPanelSettings(ViewMode defaultViewMode, OperationMode defaultOperationMode, boolean confirmDelete, boolean stayInAddMode, boolean goToLastAdded) {
        this.defaultViewMode = defaultViewMode;
        this.defaultOperationMode = defaultOperationMode;
        this.confirmDelete = confirmDelete;
        this.stayInAddMode = stayInAddMode;
        this.goToLastAdded = goToLastAdded;
    }

    public StdPanelSettings() {
    }

    public boolean isConfirmDelete() {
        return confirmDelete;
    }

    public void setConfirmDelete(boolean confirmDelete) {
        this.confirmDelete = confirmDelete;
    }

    public OperationMode getDefaultOperationMode() {
        return defaultOperationMode;
    }

    public void setDefaultOperationMode(OperationMode defaultOperationMode) {
        this.defaultOperationMode = defaultOperationMode;
    }

    public ViewMode getDefaultViewMode() {
        return defaultViewMode;
    }

    public void setDefaultViewMode(ViewMode defaultViewMode) {
        this.defaultViewMode = defaultViewMode;
    }

    public boolean isGoToLastAdded() {
        return goToLastAdded;
    }

    public void setGoToLastAdded(boolean goToLastAdded) {
        this.goToLastAdded = goToLastAdded;
    }

    public boolean isStayInAddMode() {
        return stayInAddMode;
    }

    public void setStayInAddMode(boolean stayInAddMode) {
        this.stayInAddMode = stayInAddMode;
    }
}
