package uno.d1s.slash.annotation.configuration

import org.springframework.context.annotation.Import
import uno.d1s.slash.autoconfiguration.ConfigurationPropertiesAutoConfiguration
import uno.d1s.slash.autoconfiguration.SlashAutoConfiguration
import uno.d1s.slash.autoconfiguration.jda.JdaAutoConfiguration

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(JdaAutoConfiguration::class, ConfigurationPropertiesAutoConfiguration::class, SlashAutoConfiguration::class)
public annotation class EnableSlashCommands
