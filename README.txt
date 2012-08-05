Maven Tiles
----

+ TRY IT!

Prerequisites: Maven 3.0.2+ installed, M2_HOME/bin in your path
1. mvn clean install
2. cp maven-tiles-component/target/maven-tiles-core-1.0-SNAPSHOT.jar $M2_HOME/lib (check if $M2_HOME is set first)
3. cd examples/wicket-quickstart && mvn jetty:run

+ WHAT IS MAVEN TILES

Maven Tiles is a patch for Maven 3.0.2. It tries to bring a new level of modularity to Maven, allowing
to include multiple pom snippets into your project and overcoming the bottleneck of having the project's
parent as single point of inheritance.

+ WHO'S THE TARGET

This 3-days-long spike is targeted to the Maven community, users and developers.
Users, to understand if users are willing and interested to wrap behavioural build *aspects* as independent artifacts.
Developers, for several reasons:
  - Understand if this feature could be delivered at all or if it's just a hack that won't work with
the majority of the build scenarios
  - Know if there's any ongoing discussion on similar features (e.g. Mixins), or even Jira issues where I could send
  some comments and patches
  - If it's a viable feature, I'd like to know if there's anything good in my code

I'm very open to any kind of suggestion and contribution.

+ WHAT IS A MAVEN TILE

Tiles are plain Maven pom artifacts (see ../examples/tiles/*) which contain parts of a Maven POM; every tile
represents a different build data - for example the license tags that a company wants to report on all their
artifacts - and aspects, for example build configurations and reporting.

+ FEATURE DRIVEN TILES

The most powerful advantage comes with feature-driven tiles; if we need to perform a remote deployment using
cargo, the first action is to google for "cargo-maven-plugin", find some pom.xml snippets, copy/paste,
change some properties and give it a try. Maven Tiles basically performs the copy/paste for you, labeling the snippets
with a groupIp:artifactId:version.

+ SEPARATION OF CONCERNS

Maven Tiles can deliver an additional Separation of Concern into the Software Automation market, since technology
providers could deliver - along with their artifacts (e.g. JBoss) - the build behaviors
(e.g. cargo-maven-plugin + reporting + ...) and developers will be able to use the very same tools they have available
now to browse and include them.

+ ADDITIONAL NOTES

Sometime tiles can be counter-productive to use; for example the maven-resources-tile contains the build resource
configuration, which are commonly different for each project; moving it to another file just makes it more
difficult for a dev to read it.
I've tried to use it only for test purposes, to understand if the tile merging mechanism is correctly supported
and does not cause issues to other plugins.

+ CURRENT ISSUES

- Plugin versions cannot be parametrized(!) - see maven-eclipse-tile
- Test Coverage
- Test with other Maven 3.0.x and trunk
- Improve Model Merging
(?) - Introduce a 'tile' packaging; every tile needs to specify it in order to be considered as such
(?) - Tiles must be installed and deploy, exactly like a pom artifact
(?) - The aggregator project will specify 'tile' artifacts along with the other dependencies
