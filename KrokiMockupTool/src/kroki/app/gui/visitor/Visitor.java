package kroki.app.gui.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to implement the visitor pattern.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public abstract class Visitor {

    protected List<Object> objectList = new ArrayList<Object>();

    public abstract void visit(Object object);

    public List<Object> getObjectList() {
        return objectList;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setObjectList(List objectList) {
        this.objectList = objectList;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void addAllObjects(List objectList) {
        this.objectList.addAll(objectList);
    }

    
}
