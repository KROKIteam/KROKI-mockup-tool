package kroki.profil.panel.std;

import java.io.Serializable;
import kroki.profil.property.VisibleProperty;

/**
 * <code>StdDataSettings</code> enables data filters to be defined
 * as well as the ways of sorting the data
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class StdDataSettings implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**OCL expression used to specify the subset of data being shown within the panel*/
    private String dataFilter;
    /**Defines how the data contained by a standard panel is sorted*/
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
