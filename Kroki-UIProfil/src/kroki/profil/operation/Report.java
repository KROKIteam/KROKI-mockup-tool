/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.operation;

import kroki.profil.ComponentType;
import kroki.profil.utils.settings.ReportSettings;
import kroki.profil.utils.settings.SettingsPanel;

/**
 * Stereotip  Report označava metodu za pokretanje izveštaja
 * realizovanog nekim od raspoloživih alata za kreiranje izveštaja.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SettingsPanel(ReportSettings.class)
public class Report extends BussinessOperation {

    /**Putanja do izveštaja koji se pokreće */
    private String reportName;
    /**OCL iskaz kojim se definiše opseg podataka koji se prikazuju okviru izveštaja */
    private String dataFilter;
    /**Način sortiranja podataka u okviru izveštaja*/
    private String sortBy;

    public Report(String label) {
        super(label, true, ComponentType.BUTTON);
    }

    public Report(String label, boolean visible, ComponentType componentType) {
        super(label, visible, componentType);
    }

    public Report() {
        super();
    }

    /*****************/
    /*GETERI I SETERI*/
    /*****************/
    public String getDataFilter() {
        return dataFilter;
    }

    public void setDataFilter(String dataFilter) {
        this.dataFilter = dataFilter;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
