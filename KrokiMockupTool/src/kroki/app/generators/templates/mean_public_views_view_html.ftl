<section data-ng-controller="${class.name}Controller" data-ng-init="findOne()">
  <h2>{{${class.label}.id}}</h2>
  <div data-ng-if="hasAuthorization(${class.label})">
    <a class="btn" href="/${class.label}s/{{${class.label}._id}}/edit">
      <i class="glyphicon glyphicon-edit"></i>
    </a>
    <a class="btn" data-ng-click="remove(${class.label});">
      <i class="glyphicon glyphicon-trash"></i>
    </a>
  </div>
  <form name="Form" class="form-horizontal col-md-6" role="form" novalidate>
    <#if class.attributes?has_content>
	<#list class.attributes as attr>
		<div class="form-group" ng-class="{ 'has-error' : submitted && Form.${attr.name}.$invalid }"> 
	     	 <label mean-token="'create-a_text_field_1'" class="col-md-3 control-label">${attr.name}:</label>
	     	 <div class="col-md-9">
	       		 <input name="${attr.name}" type="text" class="form-control" data-ng-model="${class.label}.${attr.name}" id="${attr.name}" readonly>
	      	 </div>
   		 </div>

    </#list>
    </#if>
    </form>
</section>