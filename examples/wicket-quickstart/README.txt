Wicket Quickstart (197: remote -> wicket-archetype-quickstart)
----

+ DESCRIPTION

This module is an example taken from the repo1.maven.org archetype catalog and it has been decomposed
in the following tiles:

- ../tiles/maven-compile-tile
- ../tiles/maven-eclipse-tile
- ../tiles/maven-jetty-tile
- ../tiles/maven-resources-tile
- ../tiles/session-license-tile
- ../tiles/session-repositories-tile

Please note the clear distinction between company-wide (session-*) and feature-driven (maven-*) tiles.

The tested goals, so far, are:

- mvn clean install (tests included)
- mvn eclipse:eclipse
- mvn jetty:run
- mvn help:effective-pom