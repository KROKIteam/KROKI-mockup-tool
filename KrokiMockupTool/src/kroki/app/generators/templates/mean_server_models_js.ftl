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
  ${attr.name}: {
    type: ${attr.jsType},
    required: ${attr.mandatory?string}
  },
	</#list>
	</#if>
 created: {
    type: Date,
    default: Date.now
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
});

/**
 * Validations
 */
<#if class.attributes?has_content>
	<#list class.attributes as attr>
		<#if attr.mandatory>
			${class.name}Schema.path('${attr.name}').validate(function(${attr.name}) {
			  return !!${attr.name};
			}, '${attr.name} cannot be blank');
		</#if>
	</#list>
	</#if>
/**
 * Statics
 */
${class.name}Schema.statics.load = function(id, cb) {
  this.findOne({
    _id: id
  }).populate('user', 'name username').exec(cb);
};

mongoose.model('${class.name}', ${class.name}Schema);
