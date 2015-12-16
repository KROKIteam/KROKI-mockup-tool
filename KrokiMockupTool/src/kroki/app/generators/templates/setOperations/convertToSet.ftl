	public Set<${metoda.iterType}> ${metoda.name}(${metoda.type} collection){
		Set<${metoda.iterType}> result = new HashSet<${metoda.iterType}>();
		Iterator<${metoda.iterType}> iter=collection.iterator();
		while(iter.hasNext()){
			result.add(iter.next());
		}
		return result;
	}