package dev.d1s.slash.registrar

import dev.d1s.slash.domain.SlashCommandDefinition

public interface SlashCommandRegistrar {

    public fun registerAll(definitions: Set<SlashCommandDefinition>)
}
