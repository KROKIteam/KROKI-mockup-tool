'use strict';

/*
 * Defining the Package
 */
var Module = require('meanio').Module;

var ${class.name}s = new Module('${class.label}');


/* attr
 * All MEAN packages require registration
 * Dependency injection is used to define required modules
 */
${class.name}s.register(function(app, auth, database, circles, swagger) {

  //We enable routing. By default the Package Object is passed to the routes
  ${class.name}s.routes(app, auth, database);
  
${class.name}s.menus.add({
    'roles': ['authenticated'],
    'title': '${class.name}s',
    'link': 'all ${class.label}s'
});

  swagger.add(__dirname);
	
  return ${class.name}s;
});