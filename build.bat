rem ********************
rem *   Requirements   * 
rem ********************
rem * Java JDK 12 
rem * Gradle 3.2 >= version < 7.x
SET GRADLE_HOME=C:\JavaBuildTools\gradle-8.0.2
SET JAVA_HOME=C:\Java\jdk-12.0.2
%GRADLE_HOME%/bin/gradle clean build -x test