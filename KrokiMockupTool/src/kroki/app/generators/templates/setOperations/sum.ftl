	public int ${metoda.name}{
		Integer result=0;
		Iterator<${metoda.iterType}> iter=${metoda.forParam}.iterator();
		while(iter.hasNext()){
			${metoda.iterType} x = (${metoda.iterType})iter.next();
			result+=${metoda.propertyInSet};
		}	
		return result;
	}