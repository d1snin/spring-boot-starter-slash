package uno.d1s.slash.registry

import uno.d1s.slash.domain.execution.SlashCommandExecution

internal interface SlashCommandExecutionRegistry {

    operator fun get(name: String, clone: Boolean = true): SlashCommandExecution

    operator fun plusAssign(slashCommandExecution: SlashCommandExecution)
}