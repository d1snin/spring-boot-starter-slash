package uno.d1s.slash.autoconfiguration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uno.d1s.slash.beanPostProcessor.SlashCommandControllerBeanPostProcessor
import uno.d1s.slash.executor.SlashCommandExecutor
import uno.d1s.slash.executor.impl.SlashCommandExecutorImpl
import uno.d1s.slash.processor.MappingProcessor
import uno.d1s.slash.processor.impl.MappingProcessorImpl
import uno.d1s.slash.registry.SlashCommandExecutionRegistry
import uno.d1s.slash.registry.impl.SlashCommandExecutionRegistryImpl

@Configuration
public class SlashAutoConfiguration {

    @Bean
    internal fun slashCommandControllerBeanPostProcessor(): SlashCommandControllerBeanPostProcessor =
        SlashCommandControllerBeanPostProcessor()

    @Bean
    internal fun slashCommandExecutor(): SlashCommandExecutor = SlashCommandExecutorImpl()

    @Bean
    internal fun slashCommandExecutionRegistry(): SlashCommandExecutionRegistry =
        SlashCommandExecutionRegistryImpl()

    @Bean
    internal fun mappingProcessor(): MappingProcessor = MappingProcessorImpl()
}
