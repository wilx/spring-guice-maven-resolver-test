package wilx.spring.guice.maven.resolver.test;

//import org.apache.maven.repository.internal.MavenRepositorySystemUtils;

import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.artifact.DefaultArtifactType;
import org.eclipse.aether.collection.DependencyGraphTransformer;
import org.eclipse.aether.collection.DependencyManager;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.collection.DependencyTraverser;
import org.eclipse.aether.impl.ArtifactResolver;
import org.eclipse.aether.internal.impl.DefaultLocalRepositoryProvider;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.NoLocalRepositoryManagerException;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.spi.localrepo.LocalRepositoryManagerFactory;
import org.eclipse.aether.util.artifact.DefaultArtifactTypeRegistry;
import org.eclipse.aether.util.graph.manager.ClassicDependencyManager;
import org.eclipse.aether.util.graph.selector.AndDependencySelector;
import org.eclipse.aether.util.graph.selector.ExclusionDependencySelector;
import org.eclipse.aether.util.graph.selector.OptionalDependencySelector;
import org.eclipse.aether.util.graph.selector.ScopeDependencySelector;
import org.eclipse.aether.util.graph.transformer.ChainedDependencyGraphTransformer;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;
import org.eclipse.aether.util.graph.transformer.JavaDependencyContextRefiner;
import org.eclipse.aether.util.graph.transformer.JavaScopeDeriver;
import org.eclipse.aether.util.graph.transformer.JavaScopeSelector;
import org.eclipse.aether.util.graph.transformer.NearestVersionSelector;
import org.eclipse.aether.util.graph.transformer.SimpleOptionalitySelector;
import org.eclipse.aether.util.graph.traverser.FatArtifactTraverser;
import org.eclipse.aether.util.repository.SimpleArtifactDescriptorPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Properties;

@Service
public class RepositoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryService.class);

    final RepositorySystem repositorySystem;
    final ArtifactResolver artifactResolver;
    final LocalRepositoryManagerFactory localRepositoryManagerFactory;

    @Autowired
    public RepositoryService(final RepositorySystem repositorySystem,
                             final ArtifactResolver artifactResolver,
                             @Qualifier("enhanced") LocalRepositoryManagerFactory lrmf) {
        this.repositorySystem = repositorySystem;
        this.artifactResolver = artifactResolver;
        this.localRepositoryManagerFactory = lrmf;
    }

    @EventListener
    public void onStart(ApplicationReadyEvent event) throws ArtifactResolutionException, NoLocalRepositoryManagerException {
        LOGGER.info("event: {}", event);
        final DefaultRepositorySystemSession repositorySystemSession = newSession();
        final Artifact artifact = new DefaultArtifact("commons-io", "commons-io", null, "2.11.0");
        final ArtifactRequest ar = new ArtifactRequest();
        ar.setArtifact(artifact);
        final ArtifactResult artifactResult = this.artifactResolver.resolveArtifact(repositorySystemSession, ar);
        LOGGER.info("artifact result: {}", artifactResult);
    }

    public DefaultRepositorySystemSession newSession() throws NoLocalRepositoryManagerException {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository lr = new LocalRepository(
                Paths.get(SystemUtils.getUserName(), "test-m2", "repository").toFile());
        final LocalRepositoryManager localRepositoryManager = this.localRepositoryManagerFactory.newInstance(session, lr);
        session.setLocalRepositoryManager(localRepositoryManager);

        return session;
    }
}
