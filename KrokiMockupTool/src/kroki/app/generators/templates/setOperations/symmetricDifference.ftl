	public Set<${metoda.iterType}> ${metoda.name}(Set<${metoda.iterType}> setA, Set<${metoda.iterType}> setB){	
		Set<${metoda.iterType}> setACopy = new HashSet<${metoda.iterType}>();
		setACopy.addAll(setA);
		setA.removeAll(setB);
		setB.removeAll(setACopy);
		setA.addAll(setB);
		return setA;
	}