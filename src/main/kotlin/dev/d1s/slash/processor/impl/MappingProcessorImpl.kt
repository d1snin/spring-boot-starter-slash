package dev.d1s.slash.processor.impl

import dev.d1s.slash.domain.ChannelTypeDefinition
import dev.d1s.slash.domain.CommandChoiceDefinition
import dev.d1s.slash.domain.OptionDefinition
import dev.d1s.slash.domain.SlashCommandDefinition
import dev.d1s.slash.domain.execution.SlashCommandExecution
import dev.d1s.slash.mapper.OptionTypeMapper
import dev.d1s.slash.processor.MappingProcessor
import dev.d1s.slash.registrar.SlashCommandRegistrar
import dev.d1s.slash.registry.SlashCommandExecutionRegistry
import dev.d1s.slash.util.findOptionAnnotation
import dev.d1s.teabag.logging.logger
import org.apache.commons.lang3.reflect.MethodUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
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

    private val definitions: MutableSet<SlashCommandDefinition> = CopyOnWriteArraySet()

    private val log = logger(this::class)

    override fun process(execution: SlashCommandExecution) {
        val method = execution.method
        log.debug("Processing mapping: $method")

        val parameters = method.parameters
        val mapping = execution.mapping

        val options = parameters.filter {
            it.findOptionAnnotation() != null
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
                MethodUtils.getAccessibleMethod(declaringClass, customizer, SlashCommandDefinition::class.java),
                execution.obj,
                slashCommandDefinition
            )
        }

        execution.slashCommandDefinition = slashCommandDefinition
        definitions += slashCommandDefinition
        slashCommandExecutionRegistry += execution
    }

    private fun Parameter.toOptionDefinition(): OptionDefinition {
        val option = this.findOptionAnnotation()!!

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
        log.debug("Executing customizer: $method")
        method.invoke(obj, slashCommandDefinition)
    }

    @EventListener(ContextRefreshedEvent::class)
    fun registerDefinitions() {
        slashCommandRegistrar.registerAll(definitions)
        log.info("Slash commands have been initialized.")
    }
}