/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.panel.std;

import java.io.Serializable;
import kroki.profil.panel.mode.OperationMode;
import kroki.profil.panel.mode.ViewMode;

/**
 * <code>StdPanelSettings</code> definiše skup podešavanja standardnog panela koja specificiraju njegovo ponašanje.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class StdPanelSettings implements Serializable {

    /** Podrazumevani prikaz - prikaz u kome se panel nalazi prilikom prvog pojavljivanja. Moguće vrednosti su definisane nabrojanim tipom ViewMode*/
    private ViewMode defaultViewMode = ViewMode.INPUT_PANEL_MODE;
    /**Podrazumevani režim – režim u kome se panel nalazi prilikom prvog pojavljivanja. Moguće vrednosti su definisane nabrojanim tipom OperationMode*/
    private OperationMode defaultOperationMode = OperationMode.VIEW_MODE;
    /**Indikator da li je potrebno tražiti od korisnika da potvrdi brisanje reda */
    private boolean confirmDelete = false;
    /**
     * Indikator da li je posle unosa reda potrebno ostati u režimu za unos (u suprotnom se panel vraća u
     * režim za pregled). Povratak u režim za pregled posle unosa jednog reda se obično primenjuje kod
     * kreiranja dokumenata gde se posle unosa zaglavlja (jedan red) odmah prelazi na unos stavki.
     */
    private boolean stayInAddMode = false;
    /** Indikator da li je posle izlaska iz režima za unos potrebno označiti poslednji uneti red (u  suprotnom se označava  prvi red u tabeli) */
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
