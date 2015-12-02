	public List<${metoda.type}> ${metoda.name}{
		List<${metoda.type}> result = new ArrayList<${metoda.type}>();
		Iterator<${metoda.iterType}> iter=${metoda.forParam}.iterator();
		while(iter.hasNext()){
			${metoda.iterType} x = (${metoda.iterType})iter.next();
			result.add(${metoda.ifCondition});
		}
		return result;
	}