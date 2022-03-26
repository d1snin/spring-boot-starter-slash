package dev.d1s.slash.autoconfiguration

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan("dev.d1s.slash.properties")
public class ConfigurationPropertiesAutoConfiguration
