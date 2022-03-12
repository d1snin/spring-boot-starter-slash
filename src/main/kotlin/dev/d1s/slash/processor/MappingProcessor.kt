package dev.d1s.slash.processor

import dev.d1s.slash.domain.execution.SlashCommandExecution

internal interface MappingProcessor {

    fun process(execution: SlashCommandExecution)
}
