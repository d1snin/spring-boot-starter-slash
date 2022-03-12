package dev.d1s.slash.testUtil

import dev.d1s.slash.domain.execution.SlashCommandExecution
import dev.d1s.slash.mock.mockSlashCommandDefinition

internal fun SlashCommandExecution.setMockSlashCommandDefinition() = apply {
    slashCommandDefinition = mockSlashCommandDefinition
}