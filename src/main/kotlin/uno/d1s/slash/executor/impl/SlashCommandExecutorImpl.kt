package uno.d1s.slash.executor.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import uno.d1s.slash.annotation.Option
import uno.d1s.slash.domain.execution.InjectableOption
import uno.d1s.slash.domain.execution.InjectableParameter
import uno.d1s.slash.executor.SlashCommandExecutor
import uno.d1s.slash.processor.ReturnValueProcessor
import uno.d1s.slash.registry.SlashCommandExecutionRegistry
import uno.d1s.slash.util.logger

internal class SlashCommandExecutorImpl : SlashCommandExecutor {

    @Autowired
    private lateinit var slashCommandExecutionRegistry: SlashCommandExecutionRegistry

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val logger = logger()

    override fun execute(
        commandName: String,
        injectableParameters: Set<InjectableParameter>,
        injectableOptions: Set<InjectableOption>,
        returnValueProcessor: ReturnValueProcessor
    ) {
        val execution = slashCommandExecutionRegistry[commandName]

        if (injectableOptions != execution.injectableOptions) {
            throw IllegalArgumentException("Injectable options are conflicting with predefined ones.")
        }

        injectableParameters.forEach {
            it.obj ?: throw IllegalArgumentException("Parameter's value can not be null.")
        }

        injectableOptions.forEach {
            it.obj ?: throw IllegalArgumentException("Option's value can not be null.")
        }

        val method = execution.method

        val returnValue = method.invoke(
            applicationContext.getBean(method.declaringClass),
            *method.parameters.map {
                val option = AnnotationUtils.findAnnotation(it, Option::class.java)

                option?.let {
                    injectableOptions.first { injectableOption ->
                        injectableOption.name == option.name
                    }.obj!!
                } ?: run {
                    (injectableParameters.firstOrNull { injectableParameter ->
                        injectableParameter.type == it.type
                    }
                        ?: throw IllegalStateException("Parameter value of this type could not be injected: ${it.type}")).obj!!
                }
            }.toTypedArray()
        )

        if (returnValue != null) {
            if (returnValue::class.java !in returnValueProcessor.supportedTypes()) {
                logger.warn("Return type is not supported by the processor:  It is ignored.")
            } else {
                returnValueProcessor.processValue(returnValue)
            }
        }
    }
}
