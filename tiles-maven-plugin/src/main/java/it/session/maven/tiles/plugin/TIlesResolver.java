/***********************************************************************************************************************
 *
 * Maven Tiles
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/
package it.session.maven.tiles.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;

import java.io.File;

public class TIlesResolver {

  public Artifact getArtifactFromCoordinates(String groupId, String artifactId, String version) {
    return new DefaultArtifact(groupId, artifactId, TilesUtils.TILE_EXTENSION, version);
  }

  public ArtifactRequest getArtifactRequestFromArtifact(Artifact tileArtifact, MavenProject mavenProject) {
    ArtifactRequest request = new ArtifactRequest();
    request.setArtifact(tileArtifact);
    request.setRepositories(mavenProject.getRemoteProjectRepositories());
    return request;
  }

  public File resolveArtifact(MavenProject currentProject,
                                 String groupId,
                                 String artifactId,
                                 String version,
                                 RepositorySystemSession repositorySystemSession,
                                 RepositorySystem repositorySystem) throws MojoExecutionException {
    try {
      Artifact tileArtifact = getArtifactFromCoordinates(groupId, artifactId, version);
      ArtifactRequest request = getArtifactRequestFromArtifact(tileArtifact, currentProject);
      ArtifactResult result = repositorySystem.resolveArtifact(repositorySystemSession, request);
      return result.getArtifact().getFile();
    } catch (ArtifactResolutionException e) {
      throw new MojoExecutionException(String.format("Error resolving artifact %s:%s:%s", groupId, artifactId, version));
    }
  }

}
