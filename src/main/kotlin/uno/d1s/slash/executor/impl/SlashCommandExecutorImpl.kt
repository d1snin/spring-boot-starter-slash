package uno.d1s.slash.executor.impl

import dev.d1s.teabag.logging.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import uno.d1s.slash.domain.execution.InjectableOption
import uno.d1s.slash.domain.execution.InjectableParameter
import uno.d1s.slash.executor.SlashCommandExecutor
import uno.d1s.slash.processor.ReturnValueProcessor
import uno.d1s.slash.registry.SlashCommandExecutionRegistry
import uno.d1s.slash.util.getOptionAnnotation

internal class SlashCommandExecutorImpl : SlashCommandExecutor {

    @Autowired
    private lateinit var slashCommandExecutionRegistry: SlashCommandExecutionRegistry

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val log = logger

    override fun execute(
        commandName: String,
        injectableParameters: Set<InjectableParameter>,
        injectableOptions: Set<InjectableOption>,
        returnValueProcessor: ReturnValueProcessor
    ) {
        val execution = slashCommandExecutionRegistry[commandName]

        injectableParameters.forEach {
            it.obj ?: throw IllegalArgumentException("Parameter's value can not be null.")
        }

        val method = execution.method

        val returnValue = method.invoke(
            applicationContext.getBean(method.declaringClass),
            *method.parameters.map {
                val option = it.getOptionAnnotation()

                if (option != null) {
                    injectableOptions.firstOrNull { injectableOption ->
                        injectableOption.name == option.name
                    }?.obj
                } else {
                    (injectableParameters.firstOrNull { injectableParameter ->
                        injectableParameter.type == it.type
                    } ?: throw IllegalStateException(
                        "Parameter value of this type could not be injected: ${it.type}"
                    )).obj!!
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
