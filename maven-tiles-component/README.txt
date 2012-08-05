Maven Core Patch
----

+ DESCRIPTION

This is the code that does the job; it basically
- Parses the project's Maven Model, finding all properties starting with "tile.X", where X is the number that specifies the loading order of the tile
- Fetches the tiles .pom files, parsing the property value as follows: "<groupId>:<artifactId>:<version>"
- Merges the running project's Model with the Tile Model (using ModelMerger, an internal Maven class)

+ IMPLEMENTATION

I've implemented an AbstractMavenLifecycleParticipant called TilesMavenLifecycleParticipant, implementing
the logic exposed above; I've registered the component in src/main/resources/META-INF/plexus/components.xml

+ NOTES

Although the main code injection within the Maven lifecycle mechanism seems to be fitting, Maven Tiles is
still a hack of a couple of sleepless nights.