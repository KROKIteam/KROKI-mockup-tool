<section data-ng-controller="${class.name}Controller" data-ng-init="findOne()">
  <form name="Form" class="form-horizontal col-md-6" role="form" data-ng-submit="update(Form.$valid)" novalidate>
    <div class="form-group" ng-class="{ 'has-error' : submitted && Form.title.$invalid }">
      <label mean-token="'edit-title'" for="title" class="col-md-2 control-label">Title</label>
      <div class="col-md-10">
        <input name="title" type="text" class="form-control" data-ng-model="${class.label}.title" id="title" placeholder="Title" required>
        <div ng-show="submitted && Form.title.$invalid" class="help-block">
          <p ng-show="Form.title.$error.required">Title is required</p>
        </div>
      </div>

    </div>
    <div class="form-group" ng-class="{ 'has-error' : submitted && Form.content.$invalid }">
      <label mean-token="'edit-content'" for="content" class="col-md-2 control-label">Content</label>
      <div class="col-md-10">
        <textarea name="content" data-ng-model="${class.label}.content" id="content" cols="30" rows="10" placeholder="Content" class="form-control" required></textarea>
        <div ng-show="submitted && Form.content.$invalid" class="help-block">
          <p ng-show="Form.content.$error.required">Content is required</p>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label mean-token="'permissions'" for="permissions" class="col-md-2 control-label">Permissions</label>
      <div class="col-md-10">
         <ui-select multiple ng-model="${class.label}.permissions" on-select="selectPermission()">
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