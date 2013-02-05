${doc}

<persistence xmlns="http://java.sun.com/xml/ns/persistence"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://java.sun.com/xml/ns/persistence
    http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd"
   version="1.0">
   <persistence-unit name="adapt" transaction-type="RESOURCE_LOCAL">
   	  <provider>org.hibernate.ejb.HibernatePersistence</provider>
      <properties>
          <!-- Scan for annotated classes and Hibernate mapping XML files -->
          <property name="hibernate.archive.autodetection" value="class, hbm"/>
          <property name="hibernate.connection.driver_class" value="${driver}" />
			<property name="hibernate.connection.url" value="${url}" />
			<property name="hibernate.connection.username" value="${username}" />
			<property name="hibernate.connection.password" value="${password}" />
	        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL5InnoDBDialect" />
		  	<property name="hibernate.hbm2ddl.auto" value="create"/> 
      </properties>
   </persistence-unit>
</persistence>