# mint-mint
Mint mapping tool

### How to debug with IntelliJ - Step by step guide

#### Prerequisites

##### Postgres 9.X

1. Install postgres from http://www.postgresql.org/download/
2. Enter postgres with user postgres
3. `CREATE USER mint2 WITH PASSWORD '<your password>';`
4. `CREATE DATABASE mint2 ENCODING 'UTF-8' TEMPLATE template0  OWNER mint2;`
5. `CREATE SCHEMA mint2 AUTHORIZATION mint2;`
6. `ALTER ROLE mint2 SET search_path TO mint2,public;`
7. `GRANT ALL ON DATABASE mint2 TO mint2;`
8. Enter postgres with user mint2
9. Use mint2 db: `\connect mint2`
10. Use mint2 schema:` set schema 'mint2';`
11. Execute sql script: `\i <MINT_SOURCE_CODE>\WEB-INF\src\createSchema.sql`
12. Execute sql script: `\i <MINT_SOURCE_CODE>\WEB-INF\src\schemaUpdates.sql`

##### Tomcat 8

1. Download Tomcat 8 from http://tomcat.apache.org/download-80.cgi
2. Configure `$CATALINA_BASE/conf/tomcat-users.xml` to have access to Tomcat Manager Application.
```xml
<user username="ialhi" password="ialhi@mint12345" roles="manager-script"/>
```
More info [here](https://tomcat.apache.org/tomcat-8.0-doc/manager-howto.html#Configuring_Manager_Application_Access) 

#### Setup IntelliJ to debug
1. Clone the project.
2. In the directory `<MINT_SOURCE_CODE>\WEB-INF\deploy\ialhi@localhost` edit `hibernate.properties` and set the login 
   credentials for postgres.
3. Launch IntelliJ -> Import Project -> Import project from external model -> Eclipse <br />We can leave the default settings. At project SDK window, select Java 1.8. <br/>
(If it doesn't exist, install and add Java 8 Runtime)<br/>
Frameworks used by mint will be detected automatically. 
4. In `build-ialhi.xml` configure the following properties:
   + `tomcat.home`: Path to your tomcat installation
   + `ant.home`: Path to your ant installation
   + `username`: Username of the tomcat manager application user.
   + `password`: Password of the tomcat manager application user. 
5. Add ant targets defined in `build-iahli.xml` to the Ant Build.
6. Select Run -> Edit Configurations...<br/>
   Add Tomcat Server -> Remote<br/>
   Configure Application Server (You have to select the directory of the previously installed Tomcat 8)
7. Configure before launch tasks:
    + Remove the default 'make'
    + Add the following Ant tasks: deploy, reload
8. Select Startup/Connection tab -> Debug<br />
   Setup a port which Tomcat will use to serve as the debug listener. (In our case 65222)
9. Launch Tomcat in the following way:
```shell
export JPDA_ADDRESS=65222
export JPDA_TRANSPORT=dt_socket
$CATALINA_BASE/bin/catalina.sh jpda start
```
Finally:
+ Set up some breakpoints and click debug.
+ Go to: http://localhost:8080<br/>
(Hint: To quickly see if it's working place a breakpoint in the file: 
`<MINT_SOURCE_CODE>\WEB-INF\src\java\gr\ntua\ivml\mint\actions\Home.java`<br />
This is the action which is fired upon successful login)
