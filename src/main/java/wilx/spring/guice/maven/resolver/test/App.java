package wilx.spring.guice.maven.resolver.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

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
