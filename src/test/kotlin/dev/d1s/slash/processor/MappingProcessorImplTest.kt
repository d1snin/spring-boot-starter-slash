package dev.d1s.slash.processor

import com.ninjasquad.springmockk.MockkBean
import dev.d1s.slash.domain.OptionTypeDefinition
import dev.d1s.slash.mapper.OptionTypeMapper
import dev.d1s.slash.mock.ValidSlashCommandController
import dev.d1s.slash.mock.mockSlashCommandDefinition
import dev.d1s.slash.mock.mockSlashCommandExecution
import dev.d1s.slash.processor.impl.MappingProcessorImpl
import dev.d1s.slash.registrar.SlashCommandRegistrar
import dev.d1s.slash.registry.SlashCommandExecutionRegistry
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [MappingProcessorImpl::class])
internal class MappingProcessorImplTest {

    @Autowired
    private lateinit var mappingProcessorImpl: MappingProcessorImpl

    @MockkBean(relaxUnitFun = true)
    private lateinit var slashCommandRegistrar: SlashCommandRegistrar

    @MockkBean
    private lateinit var optionTypeMapper: OptionTypeMapper

    @MockkBean(relaxUnitFun = true)
    private lateinit var slashCommandExecutionRegistry: SlashCommandExecutionRegistry

    private val execution = spyk(mockSlashCommandExecution(true))
    private val expectedDefinition = mockSlashCommandDefinition

    @BeforeEach
    fun setup() {
        every {
            optionTypeMapper.mapType(String::class.java)
        } returns OptionTypeDefinition.STRING
    }

    @Test
    fun `should process slash command mapping`() {
        assertDoesNotThrow {
            mappingProcessorImpl.process(execution)
        }

        this.verifyProcessingPipeline()
    }

    @Test
    fun `should register slash command definitions`() {
        assertDoesNotThrow {
            mappingProcessorImpl.registerDefinitions()
        }

        verify {
            slashCommandRegistrar.registerAll(setOf(expectedDefinition))
        }
    }

    @Test
    fun `should execute the customizer`() {
        assertDoesNotThrow {
            mappingProcessorImpl.process(execution)
        }

        ValidSlashCommandController.verifyCustomizerCall()
    }

    private fun verifyProcessingPipeline() {
        verify {
            execution.method
        }

        verify {
            execution.mapping
        }

        verify {
            slashCommandExecutionRegistry += execution
        }
    }
}