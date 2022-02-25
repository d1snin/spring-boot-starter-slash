package uno.d1s.slash.domain.execution

import uno.d1s.slash.annotation.SlashCommandMapping
import uno.d1s.slash.domain.SlashCommandDefinition
import java.lang.reflect.Method

internal data class SlashCommandExecution(
    val method: Method,
    val mapping: SlashCommandMapping
) {
    fun clone(): SlashCommandExecution = SlashCommandExecution(method, mapping).apply {
        this.injectableParameters += this@SlashCommandExecution.injectableParameters
        this.injectableOptions += this@SlashCommandExecution.injectableOptions
        this.slashCommandDefinition = this@SlashCommandExecution.slashCommandDefinition
    }

    val injectableParameters: MutableSet<InjectableParameter> = mutableSetOf()
    val injectableOptions: MutableSet<InjectableOption> = mutableSetOf()

    lateinit var slashCommandDefinition: SlashCommandDefinition
}
