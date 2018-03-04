package kroki.profil.panel.mode;

import java.io.Serializable;

import kroki.intl.Intl;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public enum OperationMode implements Serializable{

    ADD_MODE, UPDATE_MODE, COPY_MODE, SEARCH_MODE, VIEW_MODE;

    @Override
    public String toString() {
        String key = "operationMode" + "." + name();
        return Intl.getValue(key);
    }
}
