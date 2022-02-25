package uno.d1s.slash.listener

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import org.springframework.beans.factory.annotation.Autowired
import uno.d1s.slash.domain.execution.InjectableOption
import uno.d1s.slash.domain.execution.InjectableParameter
import uno.d1s.slash.executor.SlashCommandExecutor
import uno.d1s.slash.processor.ReturnValueProcessor

internal class JdaSlashCommandInteractionEventListener : ListenerAdapter() {

    @Autowired
    private lateinit var slashCommandExecutor: SlashCommandExecutor

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val textChannel = event.textChannel

        slashCommandExecutor.execute(
            event.name,
            buildSet {
                addAll(
                    setOf(
                        InjectableParameter.of(event.jda),
                        InjectableParameter.of(event.interaction),
                        InjectableParameter.of(event.channel),
                        InjectableParameter.of(event.guildChannel),
                        InjectableParameter.of(textChannel),
                        InjectableParameter.of(event.user)
                    )
                )
            },
            event.options.map {
                InjectableOption(
                    it.name
                ).apply {
                    obj =
                        when (it.type) {
                            OptionType.STRING -> it.asString
                            OptionType.INTEGER -> it.asLong
                            OptionType.BOOLEAN -> it.asBoolean
                            OptionType.USER -> it.asUser
                            OptionType.CHANNEL -> it.asGuildChannel
                            OptionType.ROLE -> it.asRole
                            OptionType.MENTIONABLE -> it.asMentionable
                            OptionType.NUMBER -> it.asDouble
                            else -> {
                                throw IllegalStateException("Option type is not supported.")
                            }
                        }
                }
            }.toSet(),
            object : ReturnValueProcessor {

                override fun processValue(any: Any) {
                    textChannel.sendMessage(any as String).queue()
                }

                override fun supportedTypes(): Set<Class<*>> = setOf(String::class.java)
            }
        )
    }
}