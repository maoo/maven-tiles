Maven Core Patch
----

+ DESCRIPTION

This is the code that does the job; it basically
- Parses the project's Maven Model, finding the tiles-maven-plugin definition
- Fetches the tiles pom, creating a Maven Model from it (Tile Model)
- Merges the running project's Model with the Tile Model (using ModelMerger, an internal Maven class)

+ IMPLEMENTATION

I've implemented an AbstractMavenLifecycleParticipant called TilesMavenLifecycleParticipant, implementing
the logic exposed above; I've registered the component in src/main/resources/META-INF/plexus/components.xml

+ NOTES

Although the main code injection within the Maven lifecycle mechanism seems to be fitting, Maven Tiles is
still a hack of a couple of sleepless nights.