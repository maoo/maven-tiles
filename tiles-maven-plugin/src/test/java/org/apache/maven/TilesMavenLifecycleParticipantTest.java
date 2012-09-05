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
package org.apache.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.util.DefaultRepositorySystemSession;
import org.sonatype.aether.util.artifact.DefaultArtifact;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TilesMavenLifecycleParticipantTest {

  TilesMavenLifecycleParticipant participant;
  RepositorySystem mockRepositorySystem;
  RepositorySystemSession defaultRepositorySystemSession;

  @Before
  public void setupParticipant() {
    this.participant = new TilesMavenLifecycleParticipant();
    this.mockRepositorySystem = mock(RepositorySystem.class);
    this.defaultRepositorySystemSession = new DefaultRepositorySystemSession();
    participant.setRepositorySystem(mockRepositorySystem);
  }


  @Test
  public void testGetArtifactFromCoordinates() {
    Artifact artifact = participant.getArtifactFromCoordinates("dummy", "dummy", "1");
    assertNotNull(artifact);
    assertEquals(artifact.getGroupId(), "dummy");
    assertEquals(artifact.getArtifactId(), "dummy");
    assertEquals(artifact.getVersion(), "1");
  }

  @Test
  public void testGetArtifactRequestFromArtifact() {
    Artifact artifact = participant.getArtifactFromCoordinates("dummy", "dummy", "1");
    MavenProject mockMavenProject = mock(MavenProject.class);
    ArtifactRequest request = participant.getArtifactRequestFromArtifact(artifact, mockMavenProject);
    assertNotNull(request);
  }

  @Test
  public void testResolveArtifact() throws ArtifactResolutionException, MojoExecutionException, IOException {
    MavenProject emptyMavenProject = new MavenProject();

    ArtifactResult dummyArtifactResult = new ArtifactResult(new ArtifactRequest());
    DefaultArtifact dummyArtifact = new DefaultArtifact("dummy:dummy:1");
    dummyArtifactResult.setArtifact(dummyArtifact.setFile(new File("pom.xml")));

    this.mockRepositoryWithProvidedArtifact(dummyArtifactResult);

    File artifactFile = this.participant.resolveArtifact(emptyMavenProject, "dummy", "dummy", "1", this.defaultRepositorySystemSession);
    assertNotNull(artifactFile);
  }

  @Test
  public void testMergeTile() throws MavenExecutionException, IOException, ArtifactResolutionException {
    MavenProject mavenProject = new MavenProject();
    mavenProject.getProperties().setProperty("tile.test","it.session.maven.tile:session-repositories-tile:1.0-SNAPSHOT");

    ArtifactResult tileArtifactResult = new ArtifactResult(new ArtifactRequest());
    DefaultArtifact dummyArtifact = new DefaultArtifact("it.session.maven.tile:session-repositories-tile:1.0-SNAPSHOT");
    tileArtifactResult.setArtifact(dummyArtifact.setFile(new File("tiles-maven-plugin/src/test/resources/licenses-tile-pom.xml")));

    this.mockRepositoryWithProvidedArtifact(tileArtifactResult);

    assertTrue(mavenProject.getLicenses().size() == 0);
    participant.mergeTile(mavenProject,"tile.test",defaultRepositorySystemSession);
    assertTrue(mavenProject.getLicenses().size() != 0);
  }

  private void mockRepositoryWithProvidedArtifact(ArtifactResult artifactResult) throws ArtifactResolutionException {
    when(this.mockRepositorySystem.resolveArtifact(same(defaultRepositorySystemSession), any(ArtifactRequest.class))).thenReturn(artifactResult);
  }

}
