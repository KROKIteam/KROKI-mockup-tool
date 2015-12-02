	public Boolean ${metoda.name}{
		Iterator<${metoda.iterType}> iter=${metoda.forParam}.iterator();
		int count=0;
		while(iter.hasNext()){
			${metoda.iterType} x = (${metoda.iterType})iter.next();
			if${metoda.ifCondition}
				count++;
		}
		if(count==1)
			return true;
		else
			return false;
	}