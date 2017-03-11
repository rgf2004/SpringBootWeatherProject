Weather Project

	- Sample Spring Boot application
	- it is Sample Rest APIs for user/admin login & authenticate & logout
	- its APIs is 
		- Create User
		- User Login
		- User Logout
		- Admin Login
		- Admin Logout
		- Sample of Post Request To Create Database Record
	
	
	
	
- create database weatherdb, you may use the below command 
	create database weatherdb;
	
- modify the below files to add the correct username/passowrd of database as follows:

file src\main\resources\application.properties 
	jdbc.db.username
	jdbc.db.password

file flyway.properties
	flyway.user
	flyway.password
	
- run maven install to compile the application and create the database.

	in case of any error during creation process you may create it manually by running the creation scripts in the following path 

	src\main\resources\db\migration\V1_1__weatherDB_DDL.sql
	src\main\resources\db\migration\V1_2__weatherDB_DML.sql

- checking coverage as JUNIT Test leads that test coverage is ~ 75%
	
- application is a self running process depends on spring boot and doesn't require application server to run so it can be run from eclipse by running the following class

	src\main\java\com\weather\Application.java
	
	or it can be run from terminal by running the following command 
	
	java -jar weather-server-1.0-SNAPSHOT.jar
	
- you can access the application by this link http://localhost:8080/

- admin user creditinals is :
	username: admin@localweather.com
	password: 12345678
	
- there is no customers' account so you may need to create one

