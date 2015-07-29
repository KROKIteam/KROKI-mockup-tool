'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
    ${class.name} = mongoose.model('${class.name}'),
    _ = require('lodash');

module.exports = function(${class.name}) {

    return {
        /**
         * Find ${class.label} by id
         */
        ${class.label}: function(req, res, next, id) {
            ${class.name}.load(id, function(err, ${class.label}) {
                if (err) return next(err);
                if (!${class.label}) return next(new Error('Failed to load ${class.label}' + id));
                req.${class.label}= ${class.label};
                next();
            });
        },
        /**
         * Create an ${class.label}
         */
        create: function(req, res) {
            var ${class.label} = new ${class.name}(req.body);
            ${class.label}.user = req.user;

            ${class.label}.save(function(err) {
                if (err) {
                    return res.status(500).json({
                        error: 'Cannot save the ${class.label}'
                    });
                }

                ${class.name}s.events.publish('create', {
                    description: req.user.name + ' created ' + req.body.title + ' ${class.label}.'
                });

                res.json(${class.label});
            });
        },
        /**
         * Update an ${class.label}
         */
        update: function(req, res) {
            var ${class.label} = req.${class.label};

            ${class.label} = _.extend(${class.label}, req.body);


            ${class.label}.save(function(err) {
                if (err) {
                    return res.status(500).json({
                        error: 'Cannot update the ${class.label}'
                    });
                }

                ${class.name}s.events.publish('update', {
                    description: req.user.name + ' updated ' + req.body.title + ' ${class.label}.'
                });

                res.json(article);
            });
        },
        /**
         * Delete an ${class.label}
         */
        destroy: function(req, res) {
            var ${class.label} = req.${class.label};


            ${class.label}.remove(function(err) {
                if (err) {
                    return res.status(500).json({
                        error: 'Cannot delete the ${class.label}'
                    });
                }

                ${class.name}s.events.publish('remove', {
                    description: req.user.name + ' deleted ' + ${class.label}.title + ' ${class.label}.'
                });

                res.json(${class.label});
            });
        },
        /**
         * Show an ${class.label}
         */
        show: function(req, res) {

            ${class.name}s.events.publish('view', {
                description: req.user.name + ' read ' + req.${class.label}.title + ' ${class.label}.'
            });

            res.json(req.${class.label});
        },
        /**
         * List of ${class.name}s
         */
        all: function(req, res) {
            var query = req.acl.query('${class.name}');

            query.find({}).sort('-created').populate('user', 'name username').exec(function(err, ${class.label}s) {
                if (err) {
                    return res.status(500).json({
                        error: 'Cannot list the ${class.label}s'
                    });
                }

                res.json(${class.label}s)
            });

        }
    };
}
