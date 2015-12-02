	public ${metoda.type} ${metoda.header}{
		<#if metoda.type?starts_with("Set")>	
		${metoda.type} result = new Hash${metoda.type}();
		<#else>
		${metoda.type} result = new Array${metoda.type}();
		</#if> 	
		Iterator<${metoda.iterType}> iter=collection.iterator();
		while(iter.hasNext()){
			${metoda.iterType} x = (${metoda.iterType})iter.next();
			Iterator<${metoda.forParam}> iter1=x.iterator();
			while(iter1.hasNext()){
				result.add(iter1.next());
			}
		}
		return result;
	}