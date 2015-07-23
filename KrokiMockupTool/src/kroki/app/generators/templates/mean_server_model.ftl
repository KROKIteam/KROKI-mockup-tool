'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
  Schema = mongoose.Schema;


/**
 * ${class.name} Schema
 */
var ${class.name}Schema = new Schema({
<#if class.attributes?has_content>
	<#list class.attributes as attr>
  created: {
    type: ${attr.type?split(".")[2]},
    default: ${attr.type?split(".")[2]}.now
  },
  title: {
    type: ${attr.type?split(".")[2]},
    required: true,
    trim: true
  },
  content: {
    type: ${attr.type?split(".")[2]},
    required: true,
    trim: true
  },
  user: {
    type: Schema.ObjectId,
    ref: 'User'
  },
  permissions: {
    type: Array
  },
  updated: {
    type: Array
  }
	</#list>
</#if>
});

/**
 * Validations
 */
${class.name}Schema.path('title').validate(function(title) {
  return !!title;
}, 'Title cannot be blank');

${class.name}Schema.path('content').validate(function(content) {
  return !!content;
}, 'Content cannot be blank');

/**
 * Statics
 */
${class.name}Schema.statics.load = function(id, cb) {
  this.findOne({
    _id: id
  }).populate('user', 'name username').exec(cb);
};

mongoose.model('${class.name}', ${class.name}Schema);
