'use strict';

// ${class.name} authorization helpers
var hasAuthorization = function(req, res, next) {
  if (!req.user.isAdmin && !req.${class.label}.user._id.equals(req.user._id)) {
    return res.status(401).send('User is not authorized');
  }
  next();
};

var hasPermissions = function(req, res, next) {

    req.body.permissions = req.body.permissions || ['authenticated'];

    req.body.permissions.forEach(function(permission) {
        if (req.acl.user.allowed.indexOf(permission) === -1) {
            return res.status(401).send('User not allowed to assign ' + permission + ' permission.');
        };
    });

    next();
};

module.exports = function(${class.name}s, app, auth) {
  
  var ${class.label}s = require('../controllers/${class.label}s')(${class.name}s);

  app.route('/api/${class.label}s')
    .get(${class.label}s.all)
    .post(auth.requiresLogin, hasPermissions, ${class.label}s.create);
  app.route('/api/${class.label}s/:${class.label}Id')
    .get(auth.isMongoId, ${class.label}s.show)
    .put(auth.isMongoId, auth.requiresLogin, hasAuthorization, hasPermissions, ${class.label}s.update)
    .delete(auth.isMongoId, auth.requiresLogin, hasAuthorization, ${class.label}s.destroy);

  // Finish with setting up the ${class.label}Id param
  app.param('${class.label}Id', ${class.label}s.${class.label});
};
