#!/usr/bin/env sh
export JAVA_HOME="/path/to/java8"
export MVN_HOME="/path/to/maven"

export PATH="$JAVA_HOME/bin:$MVN_HOME/bin:$PATH"

mvn javadoc:aggregate