package uno.d1s.slash.domain

public data class SlashCommandDefinition(
    var name: String,
    var description: String,
    var guildId: String?,
    var enabledByDefault: Boolean,
    var options: Set<OptionDefinition>
)
