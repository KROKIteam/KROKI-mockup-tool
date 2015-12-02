	public ${metoda.type} ${metoda.name}{
		<#if metoda.type?starts_with("Set")>	
		${metoda.type} result = new Hash${metoda.type}();
		<#else>
		${metoda.type} result = new Array${metoda.type}();
		</#if> 	
		Iterator<${metoda.iterType}> iter=${metoda.forParam}.iterator();
		while(iter.hasNext()){
			${metoda.iterType} x = (${metoda.iterType})iter.next();
			if(${metoda.ifCondition})
				result.add(x);
		}
		return result;
	}