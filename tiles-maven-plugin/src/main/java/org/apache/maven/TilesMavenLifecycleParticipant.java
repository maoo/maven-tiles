package org.apache.maven;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.merge.ModelMerger;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;

/**
 * Fetches all dependencies defined in the POM <properties> as follows:
 *   <properties>
 *    <tile.1>it.session.maven.tile:maven-compile-tile:1.0-SNAPSHOT</tile.1>
 *    <tile.2>it.session.maven.tile:maven-eclipse-tile:1.0-SNAPSHOT</tile.2>
 *    <tile.3>it.session.maven.tile:maven-jetty-tile:1.0-SNAPSHOT</tile.3>
 *   </properties>
 *
 * Dependencies are fetched using Aether {@link RepositorySystem}
 * Merging operation is delegated to {@link ModelMerger}
 */
@Component(role = AbstractMavenLifecycleParticipant.class, hint = "TilesMavenLifecycleParticipant")
public class TilesMavenLifecycleParticipant extends AbstractMavenLifecycleParticipant {

  protected static final String TILE_EXTENSION = "pom";
  protected static final String TILE_PROPERTY_PREFIX = "tile.";

  protected final MavenXpp3Reader reader = new MavenXpp3Reader();
  protected final ModelMerger modelMerger = new ModelMerger();

  @Requirement
  protected Logger logger;

  @Requirement
  protected RepositorySystem repositorySystem;

  protected File resolveArtifact(MavenProject currentProject,
                                 String groupId,
                                 String artifactId,
                                 String version,
                                 RepositorySystemSession repoSession) throws MojoExecutionException {
    try {
      Artifact tileArtifact = new DefaultArtifact(groupId, artifactId, TILE_EXTENSION, version);
      ArtifactRequest request = new ArtifactRequest();
      request.setArtifact(tileArtifact);
      request.setRepositories(currentProject.getRemoteProjectRepositories());
      ArtifactResult result = this.repositorySystem.resolveArtifact(repoSession, request);
      return result.getArtifact().getFile();
    } catch (ArtifactResolutionException e) {
      throw new MojoExecutionException(String.format("Error resolving artifact %s:%s:%s", groupId, artifactId, version));
    }
  }

  /**
   * Invoked after all MavenProject instances have been created.
   * <p/>
   * This callback is intended to allow extensions to manipulate MavenProjects
   * before they are sorted and actual build execution starts.
   */
  public void afterProjectsRead(MavenSession session)
      throws MavenExecutionException {
    if (session.getCurrentProject().getBuild() != null) {

      Enumeration propertyNames = session.getCurrentProject().getProperties().propertyNames();
      while (propertyNames.hasMoreElements()) {
        String propertyName = (String) propertyNames.nextElement();
        if (propertyName.startsWith(TILE_PROPERTY_PREFIX)) {
          String propertyValue = session.getCurrentProject().getProperties().getProperty(propertyName);
          StringTokenizer propertyTokens = new StringTokenizer(propertyValue, ":");

          String groupId = propertyTokens.nextToken();
          String artifactId = propertyTokens.nextToken();
          String version = propertyTokens.nextToken();

          String currentTileInformation =
              String.format("'%s:%s:%s'",
                  groupId,
                  artifactId,
                  version);

          try {

            File artifactFile = this.resolveArtifact(
                session.getCurrentProject(),
                groupId,
                artifactId,
                version,
                session.getRepositorySession());

            Model tileModel = this.reader.read(new FileInputStream(artifactFile));
            this.modelMerger.merge(session.getCurrentProject().getModel(), tileModel, false, null);
            logger.info(String.format("Loaded Maven Tile " +currentTileInformation));

          } catch (FileNotFoundException e) {
            throw new MavenExecutionException("Error loading tile " + currentTileInformation, e);
          } catch (XmlPullParserException e) {
            throw new MavenExecutionException("Error building tile " + currentTileInformation, e);
          } catch (IOException e) {
            throw new MavenExecutionException("Error parsing tile " + currentTileInformation, e);
          } catch (MojoExecutionException e) {
            throw new MavenExecutionException("Error retrieving tile " + currentTileInformation, e);
          }
        }
      }
    }
  }
}