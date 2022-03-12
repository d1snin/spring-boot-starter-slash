package dev.d1s.slash.autoconfiguration.jda

import dev.d1s.slash.generic.jda.GenericJda
import dev.d1s.slash.listener.JdaSlashCommandInteractionEventListener
import dev.d1s.slash.mapper.OptionTypeMapper
import dev.d1s.slash.mapper.impl.JdaOptionTypeMapper
import dev.d1s.slash.properties.DiscordConfigurationProperties
import dev.d1s.slash.registrar.SlashCommandRegistrar
import dev.d1s.slash.registrar.impl.JdaSlashCommandRegistrar
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
