<?xml version="1.0" encoding="UTF-8"?>

${doc}

<persistence>
	<persistence-unit name="hotelsoap" transaction-type="RESOURCE_LOCAL">
		<provider>org.hibernate.ejb.HibernatePersistence</provider>
		<properties>
			<property name="hibernate.archive.autodetection" value="class, hbm"/>
			<property name="hibernate.connection.driver_class" value="${driver}" />
			<property name="hibernate.connection.url" value="${url}" />
			<property name="hibernate.connection.username" value="${username}" />
			<property name="hibernate.connection.password" value="${password}" />
			<property name="hibernate.hbm2ddl.auto" value="create" />
			<property name="hibernate.dialect" value="org.hibernate.dialect.${dialect}" />
			<property name="hibernate.connection.useUnicode" value="true" />
			<property name="hibernate.connection.characterEncoding" value="UTF-8" />
		</properties>
	</persistence-unit>
</persistence>