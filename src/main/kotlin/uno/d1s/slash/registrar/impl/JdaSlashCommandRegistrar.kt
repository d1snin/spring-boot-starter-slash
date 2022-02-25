package uno.d1s.slash.registrar.impl

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import org.springframework.beans.factory.annotation.Autowired
import uno.d1s.slash.domain.OptionDefinition
import uno.d1s.slash.domain.OptionTypeDefinition
import uno.d1s.slash.domain.SlashCommandDefinition
import uno.d1s.slash.generic.jda.GenericJda
import uno.d1s.slash.registrar.SlashCommandRegistrar

internal class JdaSlashCommandRegistrar : SlashCommandRegistrar {

    @Autowired
    private lateinit var genericJda: GenericJda<*>

    override fun registerAll(definitions: Set<SlashCommandDefinition>) {
        genericJda.upsertCommands(
            definitions.filter {
                it.guildId == null
            }.mapToCommandData()
        )

        definitions.filter {
            it.guildId != null
        }.forEach {
            genericJda.getGuildById(it.guildId!!).upsertCommand(it.toCommandData()).queue()
        }
    }

    private fun SlashCommandDefinition.toCommandData(): CommandData =
        CommandDataImpl(name, description).apply {
            addOptions(
                this@toCommandData.options.map {
                    it.toOptionData()
                }
            )

            isDefaultEnabled = enabledByDefault
        }

    private fun List<SlashCommandDefinition>.mapToCommandData(): Set<CommandData> =
        this.map {
            it.toCommandData()
        }.toSet()

    private fun OptionDefinition.toOptionData() = OptionData(
        type.toOptionType(),
        name,
        description,
        required,
        autoComplete
    )

    private fun OptionTypeDefinition.toOptionType() = when (this) {
        OptionTypeDefinition.STRING -> OptionType.STRING
        OptionTypeDefinition.INTEGER -> OptionType.INTEGER
        OptionTypeDefinition.NUMBER -> OptionType.NUMBER
        OptionTypeDefinition.BOOLEAN -> OptionType.BOOLEAN
        OptionTypeDefinition.USER -> OptionType.USER
        OptionTypeDefinition.CHANNEL -> OptionType.CHANNEL
        OptionTypeDefinition.ROLE -> OptionType.ROLE
        OptionTypeDefinition.MENTIONABLE -> OptionType.MENTIONABLE
    }
}
