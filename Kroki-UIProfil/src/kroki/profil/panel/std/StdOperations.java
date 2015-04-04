package kroki.profil.panel.std;

import java.io.Serializable;

/**
 * <code>StdOperations</code> enables users to define which operations
 * are permitted or forbidden within a panel
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */

public class StdOperations implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**Is it permitted or forbidden to enter new data*/  
    protected boolean add = true;
    /**Is it permitted or forbidden modify existing data*/
    protected boolean update = true;
    /**Is it permitted or forbidden to copy data*/
    protected boolean copy = true;
    /**Is it permitted or forbidden to delete data*/ 
    protected boolean delete = true;
    /**Is it permitted or forbidden to perform data search*/
    protected boolean search = true;
    /**Is it permitted or forbidden to change data view (from tabular to one record one screen and vice versa*/
    protected boolean changeMode = true;
    /**Is it permitted or forbidden to perform row navigation (first, previous, next, last)*/ 
    protected boolean dataNavigation = true;

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
