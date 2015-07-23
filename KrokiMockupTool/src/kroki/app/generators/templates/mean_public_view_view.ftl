<section data-ng-controller="${class.name}Controller" data-ng-init="findOne()">
  <span>{{${class.label}.created | date:'medium'}}</span> /
  <span>{{${class.label}.user.name}}</span>
  <h2>{{${class.name}.title}}</h2>
  <div data-ng-if="hasAuthorization(${class.label})">
    <a class="btn" href="/${class.label}/{{${class.label}._id}}/edit">
      <i class="glyphicon glyphicon-edit"></i>
    </a>
    <a class="btn" data-ng-click="remove(${class.label});">
      <i class="glyphicon glyphicon-trash"></i>
    </a>
  </div>
  <div>{{${class.label}.content}}</div>
</section>