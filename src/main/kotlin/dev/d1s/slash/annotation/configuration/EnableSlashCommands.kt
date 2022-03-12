package dev.d1s.slash.annotation.configuration

import dev.d1s.slash.autoconfiguration.ConfigurationPropertiesAutoConfiguration
import dev.d1s.slash.autoconfiguration.SlashAutoConfiguration
import dev.d1s.slash.autoconfiguration.jda.JdaAutoConfiguration
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(
    JdaAutoConfiguration::class,
    ConfigurationPropertiesAutoConfiguration::class,
    SlashAutoConfiguration::class
)
public annotation class EnableSlashCommands
