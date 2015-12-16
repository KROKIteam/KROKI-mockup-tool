	public Boolean ${metoda.name}{
		Iterator<${metoda.iterType}> iter=${metoda.forParam}.iterator();
		ArrayList<Object> list=new ArrayList<Object>();
		while(iter.hasNext()){
			${metoda.iterType} x = (${metoda.iterType})iter.next();
			if(list.contains(${metoda.ifCondition}))
				return false;
			list.add(${metoda.ifCondition});
		}
		return true;	
	}