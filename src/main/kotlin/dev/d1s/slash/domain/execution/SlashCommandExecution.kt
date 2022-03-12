package dev.d1s.slash.domain.execution

import dev.d1s.slash.annotation.SlashCommandMapping
import dev.d1s.slash.domain.SlashCommandDefinition
import java.lang.reflect.Method

internal data class SlashCommandExecution(
    val method: Method,
    val obj: Any,
    val mapping: SlashCommandMapping
) {
    lateinit var slashCommandDefinition: SlashCommandDefinition

    fun clone(): SlashCommandExecution = SlashCommandExecution(method, obj, mapping).apply {
        this.slashCommandDefinition = this@SlashCommandExecution.slashCommandDefinition
    }
}
