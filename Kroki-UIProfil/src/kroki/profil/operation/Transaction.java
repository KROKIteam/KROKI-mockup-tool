package kroki.profil.operation;

import kroki.profil.ComponentType;

/**
 * Abstract stereotype which represents a complex business transaction
 * implemented as a stored procedure or a method in the middle layer of 
 * a three-layered application
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Transaction extends BussinessOperation {

	private static final long serialVersionUID = 1L;
	
	/**Indicates if it is necessary to refresh the current row once the transaction is activated*/
    private boolean refreshRow;
    /**Indicates if it is necessary to reload the panel's data*/
    private boolean refreshAll;
    /**Indicates if a user should confirm that the transaction should be started*/
    private boolean askConfirmation;
    /**Confirmation message*/
    private String confirmationMessage;
    /**Indicates if the errors (if they occur) should be shown on the screen*/
    private boolean showErrors;

    public Transaction(String label) {
        super(label);
    }

    public Transaction(String label, boolean visible, ComponentType componentType) {
        super(label, visible, componentType);
    }

    public Transaction() {
    }

    /**********************/
    /*GETTERS AND SETTERS*/
    /*********************/
    
    public boolean isAskConfirmation() {
        return askConfirmation;
    }

    public void setAskConfirmation(boolean askConfirmation) {
        this.askConfirmation = askConfirmation;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public void setConfirmationMessage(String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    public boolean isRefreshAll() {
        return refreshAll;
    }

    public void setRefreshAll(boolean refreshAll) {
        this.refreshAll = refreshAll;
    }

    public boolean isRefreshRow() {
        return refreshRow;
    }

    public void setRefreshRow(boolean refreshRow) {
        this.refreshRow = refreshRow;
    }

    public boolean isShowErrors() {
        return showErrors;
    }

    public void setShowErrors(boolean showErrors) {
        this.showErrors = showErrors;
    }
}
