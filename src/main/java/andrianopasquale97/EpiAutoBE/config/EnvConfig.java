package andrianopasquale97.EpiAutoBE.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "file:./resources/application.properties", ignoreResourceNotFound = true)
public class EnvConfig {
}
