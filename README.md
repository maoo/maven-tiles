# Maven Tiles

## TRY IT!

Prerequisites: Maven 3.0.2+ installed, M2_HOME/bin in your path

1. mvn clean install
2. cd examples/wicket-quickstart/runner && mvn jetty:run

## WHAT IS MAVEN TILES

Maven Tiles is a Maven Plugin (tiles-maven-plugin) that tries to bring a new level of modularity to Maven, allowing to include multiple pom snippets into your project and overcoming the bottleneck of having the project's parent as single point of inheritance.

Read more:
- http://jira.codehaus.org/browse/MNG-5102
- http://stackoverflow.com/questions/11749375/import-maven-plugin-configuration-by-composition-rather-than-inheritance-can-it
- http://maven.40175.n5.nabble.com/Moving-forward-with-mixins-tc4421069.html

## WHAT IS A MAVEN TILE

Tiles are [plain Maven pom artifacts](https://github.com/maoo/maven-tiles/tree/master/examples/tiles) which contain parts of a Maven POM; every tile can contain
- build data, for example the license tags that a company wants to report on all their artifacts
- build aspects, for example the runnability of a project could be defined into a jetty-maven-plugin-tile

## FEATURE DRIVEN TILES

The most powerful advantage comes with feature-driven tiles; if we need to perform a remote deployment using [cargo](http://cargo.codehaus.org/Maven2+plugin), the first action is to google for "cargo maven", find some pom.xml snippets, copy/paste into your build, change some properties and give it a try. Maven Tiles basically allows you to reuse this copy/paste and reference it across an infinite number of Maven builds; no copy/paste guaranteed!

## SEPARATION OF CONCERNS

Maven Tiles delivers an additional Separation of Concern into the Software Automation market: software products (e.g. RedHat JBoss) could deliver - along with the artifacts - the build behaviors (e.g. cargo-maven-plugin + configuration) that proved to be stable and consistent; developers will be able to browse Maven repositories and find the tiles they're interested to embed in their builds.

## ADDITIONAL NOTES

Sometime tiles can be counter-productive to use; for example the maven-resources-tile contains the build resource configuration, which are commonly different for each project; moving it to another file just makes it more difficult for a dev to read it.

A great tool for supporting Maven POM development is the [maven-help-plugin](http://maven.apache.org/plugins/maven-help-plugin); before running a tile-aggregated build, just launch [mvn help:effective-pom](http://maven.apache.org/plugins/maven-help-plugin/effective-pom-mojo.html) and analyse the final POM file that gets executed

## WHAT IS WORKING

- Aggregate [multiple tiles in one single pom](https://github.com/maoo/maven-tiles/blob/master/examples/wicket-quickstart/pom.xml)
- Tiles get [inherited from the POM parent](https://github.com/maoo/maven-tiles/blob/master/examples/wicket-quickstart/runner/pom.xml)

## WHAT NEEDS TO BE TESTED

- Maven properties defined within a tile should be visible from others
- What happens when 2 tiles define the same configuration?
- What happens when 2 tiles define 2 different executions of the same plugin?


## CURRENT ISSUES

- Plugin versions cannot be parametrized(!) - see maven-eclipse-tile
- Test Coverage
