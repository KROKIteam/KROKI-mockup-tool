exports.models = {

  User: {
    id: 'User',
    required: ['name', 'email', 'username'],
    properties: {
      name: {
        type: 'string',
        description: 'Name of the user'
      },
      email: {
        type: 'string',
        description: 'Email used for authentication and notifications'
      },
      phone: {
        type: 'string',
        description: 'Phone number of the user'
      }

    }
  },
  ${class.name}: {
    id: '${class.name}',
    required: ['content'],
    properties: {
      id: {
        type: 'number',
        description: 'Unique identifier for the ${class.name}'
      }
	<#if class.attributes?has_content>
	<#list class.attributes as attr>
	,${attr.name}: {
	 type: ${attr.type},
	 description: '${attr.name} of ${class.name}'
	}
	</#list>
	</#if>

    }
  }
};
