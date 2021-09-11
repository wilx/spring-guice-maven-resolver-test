package wilx.spring.guice.maven.resolver.test;

import com.google.inject.Guice;
import com.google.inject.Stage;
import org.eclipse.aether.impl.guice.AetherModule;
import org.eclipse.sisu.space.SpaceModule;
import org.eclipse.sisu.space.URLClassSpace;
import org.eclipse.sisu.wire.WireModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Jsr330ScopeMetadataResolver;

@EnableAutoConfiguration
@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackageClasses = App.class)
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder(App.class)
                .build(args);
        final ConfigurableApplicationContext context = app.run(args);
        context.start();
    }

}
