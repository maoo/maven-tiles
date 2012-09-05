#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License. See accompanying LICENSE file.

This project is the exact replica of [wicket-archetype-quickstart 1.5-RC4.2](http://search.maven.org/#artifactdetails%7Corg.apache.wicket%7Cwicket-archetype-quickstart%7C1.5-RC4.2%7Cmaven-archetype) that have been decomposed in the following tiles:


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

- mvn clean install
- mvn eclipse:eclipse
- mvn jetty:run
- mvn help:effective-pom