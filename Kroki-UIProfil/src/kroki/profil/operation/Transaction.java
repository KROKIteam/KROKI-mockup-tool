/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.operation;

import kroki.profil.ComponentType;

/**
 * Apstraktni stereotip  BussinesTransaction označava složenu
 * poslovnu transakciju koja se implementira kao uskladištena procedura ili
 * kao metoda u srednjem sloju nekog troslojnog rešenja.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Transaction extends BussinessOperation {

	private static final long serialVersionUID = 1L;
	
    /**indikator da je potrebno osvežiti prikaz tekućeg reda posle aktiviranja transakcije*/
    private boolean refreshRow;
    /**indikator da je potrebno ponovno učitavanje podataka u okviru panela */
    private boolean refreshAll;
    /**Indikator da je potrebno traženje potvrde od korisnika pre pokretanja transakcije */
    private boolean askConfirmation;
    /**Poruka za traženje potvrde */
    private String confirmationMessage;
    /**Indikator da li je potrebno prikazati eventualne greške na ekranu */
    private boolean showErrors;

    public Transaction(String label) {
        super(label);
    }

    public Transaction(String label, boolean visible, ComponentType componentType) {
        super(label, visible, componentType);
    }

    public Transaction() {
    }

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
