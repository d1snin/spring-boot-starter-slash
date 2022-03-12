package dev.d1s.slash.beanPostProcessor

import dev.d1s.slash.annotation.SlashCommandController
import dev.d1s.slash.domain.execution.SlashCommandExecution
import dev.d1s.slash.processor.MappingProcessor
import dev.d1s.slash.util.findSlashCommandMappingAnnotation
import dev.d1s.teabag.logging.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor

internal class SlashCommandControllerBeanPostProcessor : BeanPostProcessor {

    @Autowired
    private lateinit var mappingProcessor: MappingProcessor

    private val log = logger

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        val beanClass = bean::class.java
        if (beanClass.isAnnotationPresent(SlashCommandController::class.java)) {
            log.debug("Found the controller: $beanClass")
            beanClass.methods.forEach { method ->
                method.findSlashCommandMappingAnnotation()?.let { mapping ->
                    mappingProcessor.process(
                        SlashCommandExecution(method, bean, mapping)
                    )
                }
            }
        }
        return bean
    }
}