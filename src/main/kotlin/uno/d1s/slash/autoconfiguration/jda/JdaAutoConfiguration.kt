package uno.d1s.slash.autoconfiguration.jda

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uno.d1s.slash.generic.jda.GenericJda
import uno.d1s.slash.listener.JdaSlashCommandInteractionEventListener
import uno.d1s.slash.mapper.OptionTypeMapper
import uno.d1s.slash.mapper.impl.JdaOptionTypeMapper
import uno.d1s.slash.properties.DiscordConfigurationProperties
import uno.d1s.slash.registrar.SlashCommandRegistrar
import uno.d1s.slash.registrar.impl.JdaSlashCommandRegistrar

@Configuration
@ConditionalOnClass(JDA::class)
public class JdaAutoConfiguration {

    @Autowired
    private lateinit var discordConfigurationProperties: DiscordConfigurationProperties

    @Bean
    @ConditionalOnMissingBean
    internal fun genericJda() =
        GenericJda(
            JDABuilder.createLight(discordConfigurationProperties.token)
                .addEventListeners(this.jdaSlashCommandInteractionEventListener()).build().awaitReady()
        )

    @Bean
    @ConditionalOnMissingBean
    internal fun jdaOptionTypeMapper(): OptionTypeMapper = JdaOptionTypeMapper()

    @Bean
    @ConditionalOnMissingBean
    internal fun jdaSlashCommandRegistrar(): SlashCommandRegistrar = JdaSlashCommandRegistrar()

    @Bean
    internal fun jdaSlashCommandInteractionEventListener() = JdaSlashCommandInteractionEventListener()
}
