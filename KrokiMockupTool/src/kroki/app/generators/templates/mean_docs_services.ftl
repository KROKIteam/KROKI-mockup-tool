'use strict';

exports.load = function(swagger, parms) {

  var searchParms = parms.searchableOptions;

  var list = {
    'spec': {
      description: '${class.name} operations',
      path: '/${class.label}s',
      method: 'GET',
      summary: 'Get all ${class.label}s',
      notes: '',
      type: '${class.class}',
      nickname: 'get${class.name}s',
      produces: ['application/json'],
      params: searchParms
    }
  };

  var create = {
    'spec': {
      description: 'Device operations',
      path: '/${class.label}s',
      method: 'POST',
      summary: 'Create ${class.label}',
      notes: '',
      type: '${class.name}',
      nickname: 'create${class.name}s',
      produces: ['application/json'],
      parameters: [{
        name: 'body',
        description: '${class.name} to create.  User will be inferred by the authenticated user.',
        required: true,
        type: '${class.name}',
        paramType: 'body',
        allowMultiple: false
      }]
    }
  };

  swagger.addGet(list)
    .addPost(create);

};
