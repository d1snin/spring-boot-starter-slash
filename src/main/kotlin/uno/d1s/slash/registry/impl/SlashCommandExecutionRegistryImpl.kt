package uno.d1s.slash.registry.impl

import uno.d1s.slash.domain.execution.SlashCommandExecution
import uno.d1s.slash.registry.SlashCommandExecutionRegistry
import java.util.concurrent.CopyOnWriteArraySet

internal class SlashCommandExecutionRegistryImpl : SlashCommandExecutionRegistry {

    private val executions: MutableSet<SlashCommandExecution> = CopyOnWriteArraySet()

    override fun get(name: String, clone: Boolean): SlashCommandExecution = executions.first {
        it.slashCommandDefinition.name == name
    }.let {
        if (clone) {
            it.clone()
        } else {
            it
        }
    }

    override fun plusAssign(slashCommandExecution: SlashCommandExecution) {
        executions.add(slashCommandExecution).also { added ->
            if (!added) {
                throw IllegalArgumentException("Slash command execution already exists.")
            }
        }
    }
}