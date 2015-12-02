	public Boolean ${metoda.name}{
		Iterator<${metoda.iterType}> iter=${metoda.forParam}.iterator();
		while(iter.hasNext()){
			${metoda.iterType} x = (${metoda.iterType})iter.next();
			if(${metoda.ifCondition})
				return true;		
		}
		return false;	
	}