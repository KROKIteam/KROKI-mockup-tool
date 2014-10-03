package adapt.model.panel.configuration.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import adapt.exceptions.OperationNotFoundException;
import adapt.util.staticnames.Messages;

public class SpecificOperations {

	protected List<Operation> operations = new ArrayList<Operation>();
	
	public void add(Operation op) {
		operations.add(op);
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
	
	public Boolean hasAllowedOperations() {
		Iterator<Operation> iter = operations.iterator();
		while(iter.hasNext()) {
			if(iter.next().getAllowed()) {
				return true;
			}
		}
		return false;
	}
	
	public int getAllowedOperationCount() {
		int count = 0;
		Iterator<Operation> iter = operations.iterator();
		while(iter.hasNext()) {
			if(iter.next().getAllowed()) {
				count++;
			}
		}
		return count;
	}

	public Operation findByName(String name) throws OperationNotFoundException {
		for (Operation op : operations) {
			if(op.getName().equals(name)) {
				return op;
			}
		}
		throw new OperationNotFoundException(Messages.OPERATION_NOT_FOUND + " " + name);
	}
}