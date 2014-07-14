${doc}

<persistence xmlns="http://java.sun.com/xml/ns/persistence" version="1.0">
   <persistence-unit name="adapt" transaction-type="RESOURCE_LOCAL">
   	  <provider>org.hibernate.ejb.HibernatePersistence</provider>
      <properties>
          	<!-- Scan for annotated classes and Hibernate mapping XML files -->
          	<property name="hibernate.archive.autodetection" value="class, hbm"/>
          	<property name="hibernate.connection.driver_class" value="${driver}" />
			<property name="hibernate.connection.url" value="${url}" />
			<property name="hibernate.connection.username" value="${username}" />
			<property name="hibernate.connection.password" value="${password}" />
	        <property name="hibernate.dialect" value="org.hibernate.dialect.${dialect}" />
		  	<property name="hibernate.hbm2ddl.auto" value="create"/>
		  	<property name="hibernate.connection.useUnicode" value="true" />
			<property name="hibernate.connection.characterEncoding" value="UTF-8" />
			<property name="hibernate.show_sql" value="true" />
	  		<property name="hibernate.format_sql" value="true" />
      </properties>
   </persistence-unit>
</persistence>