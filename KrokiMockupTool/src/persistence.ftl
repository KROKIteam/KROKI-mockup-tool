<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.0" xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd">
  <persistence-unit name="hotelsoap" transaction-type="RESOURCE_LOCAL">
    <provider>org.hibernate.ejb.HibernatePersistence</provider>
    <properties>
      <property name="hibernate.connection.driver_class" value="${friver}"/>
      <property name="hibernate.connection.url" value="${url}"/>
      <property name="hibernate.connection.username" value="${username}"/>
      <property name="hibernate.connection.password" value="${password}"/>
	  <property name="hibernate.hbm2ddl.auto" value="create" />
	  <property name="hibernate.show_sql" value="true" />
	  <property name="hibernate.format_sql" value="true" />
    </properties>
  </persistence-unit>
</persistence>