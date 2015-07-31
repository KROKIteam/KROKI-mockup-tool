exports.models = {

  ${class.name}: {
    id: '${class.name}',
    required: [<#if class.attributes?has_content>
					<#list class.attributes as attr>
					<#if attr.mandatory>
						'${attr.name}'
					</#if>
					<#if attr?has_next>, </#if>
					</#list>
				</#if>],
    properties: {
      id: {
        type: 'number',
        description: 'Unique identifier for the ${class.name}'
      }
	<#if class.attributes?has_content>
	<#list class.attributes as attr>
	,${attr.name}: {
	 type: ${attr.jsType},
	 description: '${attr.name} of ${class.name}'
	}
	</#list>
	</#if>

    }
  }
};
