package wilx.spring.guice.maven.resolver.test;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelProcessor;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelProblemCollector;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.composition.DefaultDependencyManagementImporter;
import org.apache.maven.model.composition.DependencyManagementImporter;
import org.apache.maven.model.inheritance.DefaultInheritanceAssembler;
import org.apache.maven.model.inheritance.InheritanceAssembler;
import org.apache.maven.model.interpolation.ModelInterpolator;
import org.apache.maven.model.interpolation.StringVisitorModelInterpolator;
import org.apache.maven.model.io.DefaultModelReader;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.locator.DefaultModelLocator;
import org.apache.maven.model.locator.ModelLocator;
import org.apache.maven.model.management.DefaultDependencyManagementInjector;
import org.apache.maven.model.management.DefaultPluginManagementInjector;
import org.apache.maven.model.management.DependencyManagementInjector;
import org.apache.maven.model.management.PluginManagementInjector;
import org.apache.maven.model.normalization.DefaultModelNormalizer;
import org.apache.maven.model.normalization.ModelNormalizer;
import org.apache.maven.model.path.DefaultModelPathTranslator;
import org.apache.maven.model.path.DefaultModelUrlNormalizer;
import org.apache.maven.model.path.DefaultPathTranslator;
import org.apache.maven.model.path.DefaultUrlNormalizer;
import org.apache.maven.model.path.ModelPathTranslator;
import org.apache.maven.model.path.ModelUrlNormalizer;
import org.apache.maven.model.path.PathTranslator;
import org.apache.maven.model.path.UrlNormalizer;
import org.apache.maven.model.plugin.DefaultPluginConfigurationExpander;
import org.apache.maven.model.plugin.DefaultReportConfigurationExpander;
import org.apache.maven.model.plugin.DefaultReportingConverter;
import org.apache.maven.model.plugin.LifecycleBindingsInjector;
import org.apache.maven.model.plugin.PluginConfigurationExpander;
import org.apache.maven.model.plugin.ReportConfigurationExpander;
import org.apache.maven.model.plugin.ReportingConverter;
import org.apache.maven.model.profile.DefaultProfileInjector;
import org.apache.maven.model.profile.DefaultProfileSelector;
import org.apache.maven.model.profile.ProfileInjector;
import org.apache.maven.model.profile.ProfileSelector;
import org.apache.maven.model.profile.activation.FileProfileActivator;
import org.apache.maven.model.profile.activation.JdkVersionProfileActivator;
import org.apache.maven.model.profile.activation.OperatingSystemProfileActivator;
import org.apache.maven.model.profile.activation.ProfileActivator;
import org.apache.maven.model.profile.activation.PropertyProfileActivator;
import org.apache.maven.model.superpom.DefaultSuperPomProvider;
import org.apache.maven.model.superpom.SuperPomProvider;
import org.apache.maven.model.validation.DefaultModelValidator;
import org.apache.maven.model.validation.ModelValidator;
import org.apache.maven.repository.internal.MavenResolverModule;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.LocalRepositoryProvider;
import org.eclipse.aether.impl.MetadataGeneratorFactory;
import org.eclipse.aether.impl.guice.AetherModule;
import org.eclipse.aether.internal.impl.DefaultLocalRepositoryProvider;
import org.eclipse.aether.internal.impl.DefaultTrackingFileManager;
import org.eclipse.aether.internal.impl.EnhancedLocalRepositoryManagerFactory;
import org.eclipse.aether.internal.impl.SimpleLocalRepositoryManagerFactory;
import org.eclipse.aether.internal.impl.TrackingFileManager;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.spi.localrepo.LocalRepositoryManagerFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.sisu.space.SpaceModule;
import org.eclipse.sisu.space.URLClassSpace;
import org.eclipse.sisu.wire.WireModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.guice.annotation.EnableGuiceModules;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EnableGuiceModules
@Configuration
public class AppConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfiguration.class);

    //  @Autowired
//    private Injector injector;

//    @Bean
//    public WireModule wireModule() {
//        return new WireModule(                       // auto-wires unresolved dependencies
//                //new AetherModule(),
//                new SpaceModule(                     // scans and binds @Named components
//                        new URLClassSpace(App.class.getClassLoader())    // abstracts class/resource finding
//                ));
//    }

//    @Bean
//    public MavenResolverModule mavenResolverModule() {
//        return new MavenResolverModule();
//    }

    @Bean
    public MavenResolverModule guiceAetherModule() {
        return new MavenResolverModule();
    }

    @Bean
    public OurGuiceModule ourGuiceModule() {
        return new OurGuiceModule();
    }

    public void configureRepositorySystem(@Autowired final RepositorySystem repositorySystem) {

    }

//    @Bean
//    public BasicRepositoryConnectorFactory basicRepositoryConnectorFactory() {
//        return this.injector.getInstance(BasicRepositoryConnectorFactory.class);
//    }
//
//    @Bean
//    public DefaultRepositoryConnectorProvider defaultRepositoryConnectorProvider() {
//        return this.injector.getInstance(DefaultRepositoryConnectorProvider.class);
//    }

    public static class OurGuiceModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(RepositoryConnectorFactory.class)
                    .annotatedWith(Names.named("basic"))
                    .to(BasicRepositoryConnectorFactory.class);

            bind(TransporterFactory.class)
                    .annotatedWith(Names.named("file"))
                    .to(FileTransporterFactory.class);

            bind(TransporterFactory.class)
                    .annotatedWith(Names.named("http"))
                    .to(HttpTransporterFactory.class);

            bind(DependencyManagementImporter.class)
                    .to(DefaultDependencyManagementImporter.class)
                    .in(Singleton.class);

            bind(DependencyManagementInjector.class)
                    .to(DefaultDependencyManagementInjector.class)
                    .in(Singleton.class);

            bind(InheritanceAssembler.class)
                    .to(DefaultInheritanceAssembler.class)
                    .in(Singleton.class);

            bind(LifecycleBindingsInjector.class)
                    .toInstance((model, request, problems) -> {
                        // Empty.
                    });

            bind(ModelInterpolator.class)
                    .to(StringVisitorModelInterpolator.class)
                    .in(Singleton.class);

            bind(PathTranslator.class)
                    .to(DefaultPathTranslator.class)
                    .in(Singleton.class);

            bind(ModelNormalizer.class)
                    .to(DefaultModelNormalizer.class)
                    .in(Singleton.class);

            bind(ModelPathTranslator.class)
                    .to(DefaultModelPathTranslator.class)
                    .in(Singleton.class);

            bind(ModelProcessor.class)
                    .annotatedWith(Names.named("core-default"))
                    .to(DefaultModelProcessor.class)
                    .in(Singleton.class);

            bind(ModelProcessor.class)
                    .to(DefaultModelProcessor.class)
                    .in(Singleton.class);

            bind(ModelUrlNormalizer.class)
                    .to(DefaultModelUrlNormalizer.class)
                    .in(Singleton.class);

            bind(ModelValidator.class)
                    .to(DefaultModelValidator.class)
                    .in(Singleton.class);

            bind(PluginConfigurationExpander.class)
                    .to(DefaultPluginConfigurationExpander.class)
                    .in(Singleton.class);

            bind(PluginManagementInjector.class)
                    .to(DefaultPluginManagementInjector.class)
                    .in(Singleton.class);

            bind(ProfileInjector.class)
                    .to(DefaultProfileInjector.class)
                    .in(Singleton.class);

            bind(ProfileSelector.class)
                    .to(DefaultProfileSelector.class)
                    .in(Singleton.class);

            bind(ReportConfigurationExpander.class)
                    .to(DefaultReportConfigurationExpander.class)
                    .in(Singleton.class);

            bind(ReportingConverter.class)
                    .to(DefaultReportingConverter.class)
                    .in(Singleton.class);

            bind(SuperPomProvider.class)
                    .to(DefaultSuperPomProvider.class)
                    .in(Singleton.class);

            bind(ModelLocator.class)
                    .to(DefaultModelLocator.class)
                    .in(Singleton.class);

            bind(ModelReader.class)
                    .to(DefaultModelReader.class)
                    .in(Singleton.class);

            bind(UrlNormalizer.class)
                    .to(DefaultUrlNormalizer.class)
                    .in(Singleton.class);

            bind(ProfileActivator.class)
                    .annotatedWith(Names.named("file"))
                    .to(FileProfileActivator.class)
                    .in(Singleton.class);

            bind(ProfileActivator.class)
                    .annotatedWith(Names.named("jdk-version"))
                    .to(JdkVersionProfileActivator.class)
                    .in(Singleton.class);

            bind(ProfileActivator.class)
                    .annotatedWith(Names.named("os"))
                    .to(OperatingSystemProfileActivator.class)
                    .in(Singleton.class);

            bind(ProfileActivator.class)
                    .annotatedWith(Names.named("property"))
                    .to(PropertyProfileActivator.class)
                    .in(Singleton.class);

            bind(TrackingFileManager.class)
                    .to(DefaultTrackingFileManager.class)
                    .in(Singleton.class);

            bind(LocalRepositoryManagerFactory.class)
                    .annotatedWith(Names.named("enhanced"))
                    .to(EnhancedLocalRepositoryManagerFactory.class)
                    .in(Singleton.class);

            bind(LocalRepositoryManagerFactory.class)
                    .annotatedWith(Names.named("simple"))
                    .to(SimpleLocalRepositoryManagerFactory.class)
                    .in(Singleton.class);

            bind(LocalRepositoryProvider.class)
                    .to(DefaultLocalRepositoryProvider.class)
                    .in(Singleton.class);
        }

        @Provides
        public Set<RepositoryConnectorFactory> provideRepositoryConnectorFactory(
                @Named("basic") RepositoryConnectorFactory basic) {
            Set<RepositoryConnectorFactory> factories = new HashSet<>(1);
            factories.add(basic);
            return Collections.unmodifiableSet(factories);
        }

        @Provides
        public Set<TransporterFactory> provideTransporterFactory(
                @Named("file") TransporterFactory file,
                @Named("http") TransporterFactory http) {
            Set<TransporterFactory> factories = new HashSet<>(2);
            factories.add(file);
            factories.add(http);
            return Collections.unmodifiableSet(factories);
        }

        @Provides
        public List<ProfileActivator> provideProfileActivator(
                @Named("file") ProfileActivator file,
                @Named("jdk-version") ProfileActivator jdkVersion,
                @Named("os") ProfileActivator os,
                @Named("property") ProfileActivator prop
        ) {
            List<ProfileActivator> activators = new ArrayList<>(4);
            activators.add(file);
            activators.add(jdkVersion);
            activators.add(os);
            activators.add(prop);
            return Collections.unmodifiableList(activators);
        }
    }
}
