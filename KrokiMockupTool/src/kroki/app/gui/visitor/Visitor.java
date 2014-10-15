/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public abstract class Visitor {

    List<Object> objectList = new ArrayList<Object>();

    public abstract void visit(Object object);

    public List<Object> getObjectList() {
        return objectList;
    }

    public void setObjectList(List objectList) {
        this.objectList = objectList;
    }

    public void addAllObjects(List objectList) {
        this.objectList.addAll(objectList);
    }

    
}
