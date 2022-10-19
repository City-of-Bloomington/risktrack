# risktrack

this app handles city legal cases related to tort claims, worker's comp, legal actions, etc

## Introduction
1-use mvn to create a war file, such as
mvn clean package

2-Create a folder on the server that is hosting the app to include log4j2.xml file
an example of log4j2.xml files is in ./docs directory

3 - add the risktrack.xml file to your tomcat that is running your vm machine and placed
in /etc/tomcat?/Catalina/localhost/
tomcat? could be tomcat8, tomcat9, etc
an example of risktrack.xml in the ./docs folder

4-risktrack.xml files contains a number of parameters that need to be set, it is divided
into sections such CAS related, Ldap related, ADFS related, database, etc





 