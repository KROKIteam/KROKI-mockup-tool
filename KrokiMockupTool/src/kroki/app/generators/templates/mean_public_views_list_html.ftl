<section data-ng-controller="${class.name}Controller" data-ng-init="find()">
  <ul class="${class.label} unstyled">
    <li data-ng-repeat="${class.label} in ${class.label}s">
      <h2>
        <a data-ng-href="/${class.label}s/{{${class.label}._id}}">{{${class.label}._id}}</a>
        <span data-ng-if="hasAuthorization(${class.label})">
          <a class="btn" href="/${class.label}s/{{${class.label}._id}}/edit">
            <i class="glyphicon glyphicon-edit"></i>
          </a>
          <a class="btn" data-ng-click="remove(${class.label});">
            <i class="glyphicon glyphicon-trash"></i>
          </a>
        </span>
      </h2>
    </li>
  </ul>
  <h1 data-ng-hide="!${class.label}s || ${class.label}s.length">No ${class.label} yet. <br> Why don't you <a href="/${class.label}s/create">Create One</a>?</h1>
</section>