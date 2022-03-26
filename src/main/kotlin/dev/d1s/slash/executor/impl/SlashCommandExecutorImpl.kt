package dev.d1s.slash.executor.impl

import dev.d1s.slash.domain.execution.InjectableOption
import dev.d1s.slash.domain.execution.InjectableParameter
import dev.d1s.slash.executor.SlashCommandExecutor
import dev.d1s.slash.processor.ReturnValueProcessor
import dev.d1s.slash.registry.SlashCommandExecutionRegistry
import dev.d1s.slash.util.findOptionAnnotation
import dev.d1s.teabag.logging.logger
import org.springframework.beans.factory.annotation.Autowired

internal class SlashCommandExecutorImpl : SlashCommandExecutor {

    @Autowired
    private lateinit var slashCommandExecutionRegistry: SlashCommandExecutionRegistry

    private val log = logger(this::class)

    override fun execute(
        commandName: String,
        injectableParameters: Set<InjectableParameter>,
        injectableOptions: Set<InjectableOption>,
        returnValueProcessor: ReturnValueProcessor
    ) {
        val execution = slashCommandExecutionRegistry[commandName]
        val method = execution.method

        val returnValue = method.invoke(
            execution.obj,
            *method.parameters.map {
                val option = it.findOptionAnnotation()

                if (option != null) {
                    injectableOptions.firstOrNull { injectableOption ->
                        injectableOption.name == option.name
                    }?.obj ?: run {
                        if (option.required) {
                            throw IllegalStateException(
                                "Required option value could not be injected. " +
                                        "It's not provided in injectableOptions."
                            )
                        }
                        null
                    }
                } else {
                    (injectableParameters.firstOrNull { injectableParameter ->
                        injectableParameter.type == it.type
                    } ?: throw IllegalStateException(
                        "Parameter value of this type could not be injected: ${it.type}"
                    )).obj
                }
            }.toTypedArray()
        )

        if (returnValue != null) {
            val type = method.returnType

            if (type !in returnValueProcessor.supportedTypes()) {
                log.warn("Return type is not supported by the processor: $type. It is ignored.")
            } else {
                returnValueProcessor.processValue(returnValue)
            }
        }
    }
}
