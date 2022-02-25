package uno.d1s.slash.processor.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.AnnotationUtils
import uno.d1s.slash.annotation.Option
import uno.d1s.slash.domain.ChannelTypeDefinition
import uno.d1s.slash.domain.CommandChoiceDefinition
import uno.d1s.slash.domain.OptionDefinition
import uno.d1s.slash.domain.SlashCommandDefinition
import uno.d1s.slash.domain.execution.InjectableOption
import uno.d1s.slash.domain.execution.InjectableParameter
import uno.d1s.slash.domain.execution.SlashCommandExecution
import uno.d1s.slash.mapper.OptionTypeMapper
import uno.d1s.slash.processor.MappingProcessor
import uno.d1s.slash.registrar.SlashCommandRegistrar
import uno.d1s.slash.registry.SlashCommandExecutionRegistry
import uno.d1s.slash.util.logger
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet

internal class MappingProcessorImpl : MappingProcessor {

    @Autowired
    private lateinit var slashCommandRegistrar: SlashCommandRegistrar

    @Autowired
    private lateinit var optionTypeMapper: OptionTypeMapper

    @Autowired
    private lateinit var slashCommandExecutionRegistry: SlashCommandExecutionRegistry

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val definitions: MutableSet<SlashCommandDefinition> = CopyOnWriteArraySet()

    private val logger = logger()

    override fun process(execution: SlashCommandExecution) {
        val method = execution.method
        logger.debug("Processing mapping: $method")

        val parameters = method.parameters
        val mapping = execution.mapping

        val options = parameters.filter {
            it.getOptionAnnotation() != null
        }.map {
            it.toOptionDefinition()
        }.toSet()

        val slashCommandDefinition = SlashCommandDefinition(
            mapping.name,
            mapping.description,
            if (mapping.guildId == "") {
                null
            } else {
                mapping.guildId
            },
            mapping.enabledByDefault,
            options
        )

        val customizer = mapping.customizer
        if (customizer != "") {
            val declaringClass = method.declaringClass

            this.executeCustomizer(
                declaringClass.getMethod(customizer),
                applicationContext.getBean(declaringClass),
                slashCommandDefinition
            )
        }

        execution.slashCommandDefinition = slashCommandDefinition
        definitions += slashCommandDefinition

        execution.injectableOptions += options.map {
            InjectableOption(it.name)
        }

        execution.injectableParameters += parameters.filter {
            it.getOptionAnnotation() == null
        }.map {
            InjectableParameter(it.type)
        }

        slashCommandExecutionRegistry += execution
    }

    private fun Parameter.toOptionDefinition(): OptionDefinition {
        val option = this.getOptionAnnotation()!!

        return OptionDefinition(
            optionTypeMapper.mapType(type),
            option.name,
            option.description,
            option.required,
            option.autoComplete,
            if (option.channelTypes.isNotEmpty()) {
                EnumSet.copyOf(option.channelTypes.toList())
            } else {
                EnumSet.noneOf(ChannelTypeDefinition::class.java)
            },
            if (option.minValue == (-1).toLong()) {
                null
            } else {
                option.minValue
            },
            if (option.maxValue == (-1).toLong()) {
                null
            } else {
                option.maxValue
            },
            buildSet {
                addAll(option.doubleChoices.map {
                    CommandChoiceDefinition(it.name, it.value)
                })

                addAll(option.longChoices.map {
                    CommandChoiceDefinition(it.name, it.value)
                })

                addAll(option.stringChoices.map {
                    CommandChoiceDefinition(it.name, it.value)
                })
            }
        )
    }

    private fun executeCustomizer(method: Method, obj: Any, slashCommandDefinition: SlashCommandDefinition) {
        if (method.parameters.size == 1 && method.parameters[0].type == SlashCommandDefinition::class.java) {
            logger.debug("Executing customizer: $method")
            method.invoke(obj, slashCommandDefinition)
        } else {
            throw IllegalStateException(
                "Failed to execute customizer. " +
                        "The method declaration should contain only one parameter with type SlashCommandDefinition."
            )
        }
    }

    private fun Parameter.getOptionAnnotation() = AnnotationUtils.findAnnotation(this, Option::class.java)

    @EventListener(ContextRefreshedEvent::class)
    fun registerDefinitions() {
        slashCommandRegistrar.registerAll(definitions)
        logger.info("Slash commands have been initialized.")
    }
}