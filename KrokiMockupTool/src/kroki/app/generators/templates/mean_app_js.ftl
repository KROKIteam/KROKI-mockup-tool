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
Articles.register(function(app, auth, database, circles, swagger) {

  //We enable routing. By default the Package Object is passed to the routes
  Articles.routes(app, auth, database);

  Articles.aggregateAsset('css', '${class.label}.css');
  
Articles.menus.add({
    'roles': ['authenticated'],
    'title': '${class.name}s',
    'link': 'all ${class.label}s'
});

  swagger.add(__dirname);
	
  return ${class.name}s;
});