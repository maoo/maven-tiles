package org.apache.maven;

import org.apache.maven.execution.MavenSession;
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

import java.io.File;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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

    //This test is not working yet; cannot mock mockRepositorySystem.resolveArtifact
    @Test
    public void testResolveArtifact() throws ArtifactResolutionException, MojoExecutionException {
        RepositorySystem mockRepositorySystem = mock(RepositorySystem.class);
        RepositorySystemSession mockRepositorySystemSession = mock(RepositorySystemSession.class);
        Artifact artifact = participant.getArtifactFromCoordinates(groupId,artifactId,version);
        MavenProject mockMavenProject = mock(MavenProject.class);
        ArtifactRequest mockRequest = participant.getArtifactRequestFromArtifact(artifact, mockMavenProject);
        ArtifactResult mockArtifactResult = mock(ArtifactResult.class);
        participant.setRepositorySystem(mockRepositorySystem);
        when(mockRepositorySystem.resolveArtifact(mockRepositorySystemSession, mockRequest)).thenReturn(mockArtifactResult);
        File artifactFile = participant.resolveArtifact(mockMavenProject, groupId, artifactId, version, mockRepositorySystemSession);
        assertNotNull(artifactFile);
    }

}
