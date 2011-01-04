Mule Shiro Module
-----------------
A Mule SecurityManager implementation using Apache Shiro (http://shiro.apache.org).

Using the Shiro with Mule
=========================

NOTE: This currently requires Mule 3.2.0-SNAPSHOT and this is not an officially
supported MuleSoft offering.

First, you need to declare your basic Shiro security setup:

	<shiro:security-manager>
	    <shiro:delegate-security-provider name="in-memory" delegate-ref="securityManager"/>
	</shiro:security-manager>
	
	<spring:bean id="securityManager" class="org.apache.shiro.mgt.DefaultSecurityManager">
	    <spring:property name="realm" ref="myRealm"/>
	</spring:bean>
	
	<spring:bean id="myRealm" class="org.mule.module.shiro.TextConfigurationRealm">
	    <spring:property name="userDefinitions">
	      <spring:value>            
	        dan=password,administrator
	        joe=password,user
	      </spring:value>
	    </spring:property>
	    <spring:property name="roleDefinitions">
	      <spring:value>            
	        administrator=read,write
	        user=read
	      </spring:value>
	    </spring:property>
	</spring:bean>
	
	<spring:bean id="lifecycleBeanPostProcessor" class="org.apache.shiro.spring.LifecycleBeanPostProcessor"/>

There are a couple critical items:

* The Shiro realm: Realms in Shiro map to databases/property files/etc where 
  your users are stored. Shiro contains out of the box support for JDBC, LDAP,
  and text files. I’ve created my users in the XML file (very handy for 
  testing) using the TextDefinitionRealm. The userDefinitions proeprty 
  contains a list of users with their password and role. The roleDefinitions 
  property contains a list of roles and the permissions that they imply.
* The Shiro SecurityManager: this is what instantiates Shiro and holds all 
  the realms with which Shrio can do authentication.
* The Mule security manager definition: we’re simply telling Mule to use the 
  Shiro SecurityManager here.
* The Shiro bean post processor: this inits/destroys all the Shiro beans 
  properly.
  
Next up, you can implement authentication or authorization using security 
filters:

	<flow name="test">
	   <inbound-endpoint address="http://localhost/edit/something" exchange-pattern="request-response"/>
	   <http:basic-security-filter realm="mule-realm"/>
	   <shiro:authorization-filter permission="write"/>
	   ... logic which requires write access
	</flow>

Maven Repository
================
You can find builds up here:

    <repository>
        <id>codehaus</id>
        <name>Codehaus Maven Release Repository</name>
        <url>http://repository.codehaus.org</url>
    </repository>
    <repository>
        <id>codehaus-snapshots</id>
        <name>Codehaus Maven Snapshots Repository</name>
        <url>http://snapshots.repository.codehaus.org</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
    
To include the Shiro module, add the following to your dependencies:

    <dependency>
    	<groupId>org.mule.modules</groupId>
    	<artifactId>mule-module-shiro</artifactId>
    	<version>0.1-SNAPSHOT</version>
    </dependency>