package org.apache.maven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;

@RunWith(MockitoJUnitRunner.class)
public class TilesMavenLifecycleParticipantTest {

    private final String groupId = "org.apache.maven";
    private final String artifactId = "tile-test";
    private final String version = "tile-test";

    TilesMavenLifecycleParticipant participant = new TilesMavenLifecycleParticipant();

    @Test
    public void getArtifactFromCoordinatesTest() {
        Artifact artifact = participant.getArtifactFromCoordinates(groupId,artifactId,version);
        assertNotNull(artifact);
        assertEquals(artifact.getGroupId(), groupId);
        assertEquals(artifact.getArtifactId(), artifactId);
        assertEquals(artifact.getVersion(), version);
    }

    @Test
    public void getArtifactRequestFromArtifactTest() {
        Artifact artifact = participant.getArtifactFromCoordinates(groupId, artifactId, version);
        MavenProject mockMavenProject = mock(MavenProject.class);
        ArtifactRequest request = participant.getArtifactRequestFromArtifact(artifact, mockMavenProject);
        assertNotNull(request);
    }

    //This test is not working yet; cannot mtiles-maven-plugin/src/test/java/org/apache/maven/TilesMavenLifecycleParticipantTest.javaock mockRepositorySystem.resolveArtifact
    @Test
    public void testResolveArtifact() throws ArtifactResolutionException, MojoExecutionException {
        RepositorySystem mockRepositorySystem = mock(RepositorySystem.class);
        RepositorySystemSession mockRepositorySystemSession = mock(RepositorySystemSession.class);
        MavenProject emptyMavenProject = new MavenProject();
        ArtifactResult dummyArtifactResult = makeDummyArtifactResult();
        when(mockRepositorySystem.resolveArtifact(same(mockRepositorySystemSession), any(ArtifactRequest.class))).thenReturn(dummyArtifactResult);

        participant.setRepositorySystem(mockRepositorySystem);
        File artifactFile = participant.resolveArtifact(emptyMavenProject, groupId, artifactId, version, mockRepositorySystemSession);

        assertNotNull(artifactFile);
    }

	private ArtifactResult makeDummyArtifactResult() {
		ArtifactResult dummyArtifactResult = new ArtifactResult(new ArtifactRequest());
		DefaultArtifact dummyArtifact = new DefaultArtifact("dummy:dummy:1");
        dummyArtifactResult.setArtifact(dummyArtifact.setFile(new File("i_dont_exist"))); //This setFile method is not what it looks like o.O 
        return dummyArtifactResult;
	}

}
