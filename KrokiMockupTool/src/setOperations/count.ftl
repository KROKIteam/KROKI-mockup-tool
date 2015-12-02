	public int ${metoda.header}{
		int result=0;
		Iterator<${metoda.iterType}> iter=collection.iterator();
		while(iter.hasNext()){
			${metoda.iterType} x = (${metoda.iterType})iter.next();
			if(x.equals(element))
				result++;
		}
		return result;
	}