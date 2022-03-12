package dev.d1s.slash.mock

import dev.d1s.slash.domain.ChannelTypeDefinition
import dev.d1s.slash.domain.OptionDefinition
import dev.d1s.slash.domain.OptionTypeDefinition
import dev.d1s.slash.domain.SlashCommandDefinition
import dev.d1s.teabag.testing.constant.VALID_STUB
import java.util.*

internal val mockSlashCommandDefinition
    get() = SlashCommandDefinition(
        VALID_STUB, VALID_STUB, VALID_STUB,
        true,
        setOf(
            OptionDefinition(
                OptionTypeDefinition.STRING,
                VALID_STUB,
                VALID_STUB,
                required = true,
                autoComplete = false,
                EnumSet.noneOf(ChannelTypeDefinition::class.java),
                null,
                null,
                setOf()
            )
        )
    )