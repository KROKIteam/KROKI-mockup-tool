package com.panelcomposer.model.panel.configuration.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import util.staticnames.Messages;

import com.panelcomposer.exceptions.OperationNotFoundException;

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

	public boolean hasAllowedOperations() {
		Iterator<Operation> iter = operations.iterator();
		while (iter.hasNext()) {
			if (iter.next().getAllowed()) {
				return true;
			}
		}
		return false;
	}

	public int getAllowedOperationsCount() {
		int counter = 0;
		Iterator<Operation> iter = operations.iterator();
		while (iter.hasNext()) {
			if (iter.next().getAllowed()) {
				counter++;
			}
		}
		return counter;
	}

	public Operation findByName(String name) throws OperationNotFoundException {
		for (Operation op : operations) {
			if (op.getName().equals(name)) {
				return op;
			}
		}
		throw new OperationNotFoundException(
				Messages.OPERATION_NOT_FOUND + " " + name);
	}

}
