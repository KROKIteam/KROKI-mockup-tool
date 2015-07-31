<section data-ng-controller="${class.name}Controller" data-ng-init="findOne()">
  <form name="Form" class="form-horizontal col-md-6" role="form" data-ng-submit="update(Form.$valid)" novalidate>
    <#if class.attributes?has_content>
	<#list class.attributes as attr>
	    <div class="form-group" ng-class="{ 'has-error' : submitted && Form.title.$invalid }">
	      <label mean-token="'edit-${attr.name}'" for="${attr.name}" class="col-md-2 control-label">${attr.name}: </label>
	      <div class="col-md-10">
	        <input name="${attr.name}" type="text" class="form-control" data-ng-model="${class.label}.${attr.name}" id="${attr.name}" placeholder="${attr.name}" 
	        <#if attr.mandatory>required</#if>
	        ><div ng-show="submitted && Form.${attr.name}.$invalid" class="help-block">
	        
	          <p ng-show="Form.${attr.name}.$error.required">${attr.name} is required</p>
	        </div>
	      </div>
    </#list>
    </#if>
    </div>

<div class="form-group">
      <label mean-token="'permissions'" for="permissions" class="col-md-2 control-label">Permissions</label>
      <div class="col-md-10">
         <ui-select multiple ng-model="article.permissions" on-select="selectPermission()">
            <ui-select-match placeholder="Select permissions..." ng-click="showDescendants();">{{$item}}</ui-select-match>
            <ui-select-choices repeat="circle in availableCircles | filter:$select.search">
              {{circle}}
            </ui-select-choices>
          </ui-select>
      </div>
    </div>
    <div class="form-group" ng-show="descendants.length">
        <label mean-token="'descendants'" for="descendants" class="col-md-2 control-label">Descendants</label>
        <div class="col-md-10">
          <span ng-repeat="descendant in descendants">{{descendant}} <span ng-hide="$last">, </span></span>
        </div>
    </div>
    <div class="form-group">
      <div class="col-md-offset-2 col-md-10">
        <button mean-token="'edit-submit'" type="submit" class="btn btn-default">Submit</button>
      </div>
    </div>
  </form>
</section>