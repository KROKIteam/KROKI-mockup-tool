<section data-ng-controller="${class.name}Controller">
  <form name="Form" class="form-horizontal col-md-6" role="form" data-ng-submit="create(Form.$valid)" novalidate>
    
    <#if class.attributes?has_content>
	<#list class.attributes as attr>
	<div class="form-group" ng-class="{ 'has-error' : submitted && Form.${attr.name}.$invalid }"> 
      <label mean-token="'create-${attr.name}'" class="col-md-3 control-label">${attr.name}</label>
      <div class="col-md-9">
        <input name="${attr.name}" type="text" class="form-control" data-ng-model="${class.label}.${attr.name}" id="${attr.name}" placeholder="${attr.name}" required>
        <div ng-show="submitted && Form.${attr.label}.$invalid" class="help-block">
          <p ng-show="Form.${attr.name}.$error.required">${attr.name} is required</p>
        </div> 
      </div>
    </div>
	</#list>
	</#if>

    <div class="form-group">
      <label mean-token="'permissions'" for="permissions" class="col-md-3 control-label">Permissions</label>
      <div class="col-md-9">
         <ui-select multiple ng-model="${class.label}.permissions" append-to-body="'true'" on-select="selectPermission()">
            <ui-select-match placeholder="Select permissions..." ng-click="showDescendants();">{{$item}}</ui-select-match>
            <ui-select-choices recreatepeat="circle in availableCircles | filter:$select.search" name="permissions">
              {{circle}}
            </ui-select-choices>
          </ui-select>
      </div>
    </div>

	<div class="form-group" ng-show="descendants.length">
        <label mean-token="'descendants'" for="descendants" class="col-md-3 control-label">Descendants</label>
        <div class="col-md-9">
          <span ng-repeat="descendant in descendants">{{descendant}} <span ng-hide="$last">, </span></span>
        </div>
    </div>
    
    <div class="form-group">
      <div class="col-md-offset-3 col-md-9">
        <button mean-token="'create-submit'" type="submit" class="btn btn-info">Submit</button>
      </div>
    </div>
  </form>
</section>