package uno.d1s.slash.autoconfiguration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import uno.d1s.slash.properties.DiscordConfigurationProperties

@Configuration
@EnableConfigurationProperties(DiscordConfigurationProperties::class)
public class ConfigurationPropertiesAutoConfiguration
