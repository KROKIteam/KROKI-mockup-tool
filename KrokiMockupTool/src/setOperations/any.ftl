	public ${metoda.iterType} ${metoda.name}{
		ArrayList<${metoda.iterType}> result = new ArrayList<${metoda.iterType}>();
		Iterator<${metoda.iterType}> iter=${metoda.forParam}.iterator();
		while(iter.hasNext()){
			${metoda.iterType} x = (${metoda.iterType})iter.next();
			if(${metoda.ifCondition})
				result.add(x);
		}
		Random r=new Random();
		int temp=r.nextInt(result.size());
		return result.get(temp);
	}