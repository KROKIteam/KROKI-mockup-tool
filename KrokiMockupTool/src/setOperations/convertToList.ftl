	public List<${metoda.iterType}> ${metoda.name}(${metoda.type} collection){
		List<${metoda.iterType}> result = new ArrayList<${metoda.iterType}>();
		Iterator<${metoda.iterType}> iter=collection.iterator();
		while(iter.hasNext()){
			result.add(iter.next());
		}
		return result;
	}