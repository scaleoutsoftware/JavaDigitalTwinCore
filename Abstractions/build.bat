rem ********************
rem *   Requirements   * 
rem ********************
rem * Java JDK 8
rem * MVN 3.x
SET JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.452.9-hotspot
SET MVN_HOME=C:\JavaBuildTools\apache-maven-3.9.8
call mvn clean package install javadoc:jar source:jar