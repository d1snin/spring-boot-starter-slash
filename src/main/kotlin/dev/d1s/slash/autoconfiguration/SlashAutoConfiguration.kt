package dev.d1s.slash.autoconfiguration

import dev.d1s.slash.beanPostProcessor.SlashCommandControllerBeanPostProcessor
import dev.d1s.slash.executor.SlashCommandExecutor
import dev.d1s.slash.executor.impl.SlashCommandExecutorImpl
import dev.d1s.slash.processor.MappingProcessor
import dev.d1s.slash.processor.impl.MappingProcessorImpl
import dev.d1s.slash.registry.SlashCommandExecutionRegistry
import dev.d1s.slash.registry.impl.SlashCommandExecutionRegistryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
