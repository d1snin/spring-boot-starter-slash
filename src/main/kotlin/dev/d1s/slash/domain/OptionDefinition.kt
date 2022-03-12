package dev.d1s.slash.domain

import java.util.*

public data class OptionDefinition(
    var type: OptionTypeDefinition,
    var name: String,
    var description: String,
    var required: Boolean,
    var autoComplete: Boolean,
    var channelTypes: EnumSet<ChannelTypeDefinition>,
    var minvalue: Long?,
    var maxvalue: Long?,
    val choices: Set<CommandChoiceDefinition>
)
