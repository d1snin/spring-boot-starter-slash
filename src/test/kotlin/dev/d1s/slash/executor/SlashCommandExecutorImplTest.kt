package dev.d1s.slash.executor

import com.ninjasquad.springmockk.MockkBean
import dev.d1s.slash.domain.execution.InjectableOption
import dev.d1s.slash.domain.execution.InjectableParameter
import dev.d1s.slash.executor.impl.SlashCommandExecutorImpl
import dev.d1s.slash.mock.ControllerConfiguration
import dev.d1s.slash.mock.ValidSlashCommandController
import dev.d1s.slash.mock.mockSlashCommandExecution
import dev.d1s.slash.processor.ReturnValueProcessor
import dev.d1s.slash.registry.SlashCommandExecutionRegistry
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import strikt.api.expectThat
import strikt.assertions.isNull

@SpringBootTest
@ContextConfiguration(classes = [SlashCommandExecutorImpl::class, ControllerConfiguration::class])
internal class SlashCommandExecutorImplTest {

    @Autowired
    private lateinit var slashCommandExecutorImpl: SlashCommandExecutorImpl

    @MockkBean
    private lateinit var slashCommandExecutionRegistry: SlashCommandExecutionRegistry

    private val executionRequiredOption = spyk(mockSlashCommandExecution(true))
    private val executionOptionalOption = spyk(mockSlashCommandExecution(false))

    private val mockReturnValueProcessor = mockk<ReturnValueProcessor>(relaxUnitFun = true)

    @BeforeEach
    fun setup() {
        every {
            slashCommandExecutionRegistry[VALID_STUB]
        } returns executionRequiredOption

        every {
            slashCommandExecutionRegistry["_$VALID_STUB"]
        } returns executionOptionalOption

        every {
            mockReturnValueProcessor.supportedTypes()
        } returns setOf(Any::class.java)
    }

    @Test
    fun `should execute the command`() {
        assertDoesNotThrow {
            slashCommandExecutorImpl.execute(
                VALID_STUB, setOf(InjectableParameter.of(Any())), setOf(
                    InjectableOption(VALID_STUB, VALID_STUB)
                ),
                mockReturnValueProcessor
            )
        }

        this.verifyPipeline(VALID_STUB, true)

        ValidSlashCommandController.verifyCall()

        verify {
            mockReturnValueProcessor.processValue(any())
        }
    }

    @Test
    fun `should inject null value when the option is not required and not provided`() {
        assertDoesNotThrow {
            slashCommandExecutorImpl.execute(
                "_$VALID_STUB",
                setOf(InjectableParameter.of(Any())),
                setOf(), // do not provide anything
                mockReturnValueProcessor
            )
        }

        this.verifyPipeline("_$VALID_STUB", false)
        expectThat(ValidSlashCommandController.getExecutionContext()!!.testOption).isNull()

        verify {
            mockReturnValueProcessor.processValue(any())
        }
    }

    @Test
    fun `should throw IllegalStateException if the required option is not provided`() {
        assertThrows<IllegalStateException> {
            slashCommandExecutorImpl.execute(
                VALID_STUB,
                setOf(InjectableParameter.of(Any())),
                setOf(), // do not provide anything
                mockReturnValueProcessor
            )
        }

        this.verifyPipeline(VALID_STUB, true)

        ValidSlashCommandController.verifyNotCalled()
    }

    @Test
    fun `should throw IllegalStateException if the parameter can not be injected`() {
        assertThrows<IllegalStateException> {
            slashCommandExecutorImpl.execute(
                VALID_STUB,
                setOf(), // do not provide anything
                setOf(InjectableOption(VALID_STUB, VALID_STUB)),
                mockReturnValueProcessor
            )
        }

        this.verifyPipeline(VALID_STUB, true)

        ValidSlashCommandController.verifyNotCalled()
    }

    private fun verifyPipeline(slashCommandName: String, requiredOption: Boolean) {
        verify {
            slashCommandExecutionRegistry[slashCommandName]
        }

        val execution = if (requiredOption) {
            executionRequiredOption
        } else {
            executionOptionalOption
        }

        verify {
            execution.method
        }

        verify {
            execution.obj
        }
    }
}