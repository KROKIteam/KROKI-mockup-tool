<section data-ng-controller="${class.name}Controller" data-ng-init="find()">
  <ul class="${class.label} unstyled">
    <li data-ng-repeat="${class.label} in ${class.label}s">
      <span>{{ ${class.label}.created | date:'medium'}}</span> /
      <span>{{ ${class.label}.user.name}}</span>
      <h2>
        <a data-ng-href="/${class.label}/{{${class.label}._id}}">{{${class.label}.title}}</a>
        <span data-ng-if="hasAuthorization(${class.label})">
          <a class="btn" href="/${class.label}/{{${class.label}._id}}/edit">
            <i class="glyphicon glyphicon-edit"></i>
          </a>
          <a class="btn" data-ng-click="remove(${class.label});">
            <i class="glyphicon glyphicon-trash"></i>
          </a>
        </span>
      </h2>
      <div>{{${class.label}.content}}</div>
    </li>
  </ul>
  <h1 data-ng-hide="!${class.label} || ${class.label}.length">No ${class.label} yet. <br> Why don't you <a href="/${class.label}/create">Create One</a>?</h1>
</section>