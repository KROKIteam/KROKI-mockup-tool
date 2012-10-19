/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.panel.std;

import java.io.Serializable;
import kroki.profil.property.VisibleProperty;

/**
 * <code>StdDataSettings</code> omogućava definisanje filtera za podatke i načina njihovog sortiranja
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class StdDataSettings implements Serializable {

    /**OCL iskaz kojim se definiše podskup podataka koji se prikazuje u okviru datog panela.   */
    private String dataFilter;
    /**Način sortiranja podataka u okviru standardnog panela */
    private VisibleProperty sortBy;

    public StdDataSettings(String dataFilter, VisibleProperty sortBy) {
        this.dataFilter = dataFilter;
        this.sortBy = sortBy;
    }

    public StdDataSettings() {
    }

    public String getDataFilter() {
        return dataFilter;
    }

    public void setDataFilter(String dataFilter) {
        this.dataFilter = dataFilter;
    }

    public VisibleProperty getSortBy() {
        return sortBy;
    }

    public void setSortBy(VisibleProperty sortBy) {
        this.sortBy = sortBy;
    }
}
