<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">

${doc}

<hibernate-configuration>
  <session-factory>
  <property name="connection.url">${url}</property>
  <property name="connection.username">${username}</property>
  <property name="connection.password">${password}</property>
  <property name="connection.driver_class">${driver}</property>
  <property name="dialect">org.hibernate.dialect.${dialect}</property>
  <property name="transaction.factory_class">org.hibernate.transaction.JDBCTransactionFactory</property>
  <property name="current_session_context_class">thread</property>
  <!-- this will show us all sql statements -->
  <property name="hibernate.show_sql">true</property>
</session-factory>
</hibernate-configuration>