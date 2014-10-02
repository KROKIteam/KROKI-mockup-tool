/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.panel.std;

import java.io.Serializable;

/**
 * <code>StdOperations</code> omogućava definisanje standardnih
 * operacija koje su dozvoljene/zabranjene u okviru panela.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */

public class StdOperations implements Serializable{
	
	private static final long serialVersionUID = 1L;

    /**Dozvoljen/zabranjen unos novih podataka */
    private boolean add = true;
    /**Dozvoljena/zabranjena izmena podataka */
    private boolean update = true;
    /**Dozvoljeno/zabranjeno kopiranje podataka */
    private boolean copy = true;
    /**Dozvoljeno/zabranjeno brisanje podataka */
    private boolean delete = true;
    /**Dozvoljena/zabranjena pretraga podataka */
    private boolean search = true;
    /**Dozvoljena/zabranjena promena prikaza (iz tabelarnog u “jedan ekran–jedan slog” i obrnuto */
    private boolean changeMode = true;
    /**Dozvoljeno/zabranjeno kretanje kroz redove (prelazak na prvi, sledeći, prethodni i poslednji) */
    private boolean dataNavigation = true;

    public StdOperations() {
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isChangeMode() {
        return changeMode;
    }

    public void setChangeMode(boolean changeMode) {
        this.changeMode = changeMode;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    public boolean isDataNavigation() {
        return dataNavigation;
    }

    public void setDataNavigation(boolean dataNavigation) {
        this.dataNavigation = dataNavigation;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
