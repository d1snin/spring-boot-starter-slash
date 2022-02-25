package uno.d1s.slash.beanPostProcessor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.core.annotation.AnnotationUtils
import uno.d1s.slash.annotation.SlashCommandController
import uno.d1s.slash.annotation.SlashCommandMapping
import uno.d1s.slash.domain.execution.SlashCommandExecution
import uno.d1s.slash.processor.MappingProcessor
import uno.d1s.slash.util.logger

internal class SlashCommandControllerBeanPostProcessor : BeanPostProcessor {

    @Autowired
    private lateinit var mappingProcessor: MappingProcessor

    private val logger = logger()

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        val beanClass = bean::class.java

        if (beanClass.isAnnotationPresent(SlashCommandController::class.java)) {
            logger.debug("Found the controller: $beanClass")
            beanClass.methods.forEach { method ->
                AnnotationUtils.findAnnotation(method, SlashCommandMapping::class.java)?.let {
                    mappingProcessor.process(SlashCommandExecution(method, it))
                }
            }
        }
        return bean
    }
}
