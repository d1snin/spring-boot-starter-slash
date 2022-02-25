package uno.d1s.slash.processor

import uno.d1s.slash.domain.execution.SlashCommandExecution

internal interface MappingProcessor {

    fun process(execution: SlashCommandExecution)
}
