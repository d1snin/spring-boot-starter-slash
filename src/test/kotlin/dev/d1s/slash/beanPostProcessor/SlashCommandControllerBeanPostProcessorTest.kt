package dev.d1s.slash.beanPostProcessor

import com.ninjasquad.springmockk.MockkBean
import dev.d1s.slash.domain.execution.SlashCommandExecution
import dev.d1s.slash.mock.InvalidSlashCommandController
import dev.d1s.slash.mock.SlashCommandControllerWithNoMapping
import dev.d1s.slash.mock.ValidSlashCommandController
import dev.d1s.slash.processor.MappingProcessor
import dev.d1s.slash.util.findSlashCommandMappingAnnotation
import dev.d1s.slash.testUtil.setMockSlashCommandDefinition
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.reflect.jvm.javaMethod

@SpringBootTest
@ContextConfiguration(classes = [SlashCommandControllerBeanPostProcessor::class])
internal class SlashCommandControllerBeanPostProcessorTest {

    @Autowired
    private lateinit var slashCommandControllerBeanPostProcessor: SlashCommandControllerBeanPostProcessor

    @MockkBean(relaxUnitFun = true)
    private lateinit var mappingProcessor: MappingProcessor

    @Test
    fun `should process the controller`() {
        val testController = ValidSlashCommandController

        this.postProcessAndCheckReturnValue(testController)

        val testRequiredOptionMethod = testController::testRequiredOption.javaMethod!!
        val testOptionalOptionMethod = testController::testOptionalOption.javaMethod!!

        verify {
            mappingProcessor.process(
                SlashCommandExecution(
                    testRequiredOptionMethod,
                    testController,
                    testRequiredOptionMethod.findSlashCommandMappingAnnotation()!!
                ).setMockSlashCommandDefinition()
            )
        }

        verify {
            mappingProcessor.process(
                SlashCommandExecution(
                    testOptionalOptionMethod,
                    testController,
                    testOptionalOptionMethod.findSlashCommandMappingAnnotation()!!
                ).setMockSlashCommandDefinition()
            )
        }
    }

    @Test
    fun `should not process mapping`() {
        this.postProcessAndCheckReturnValue(SlashCommandControllerWithNoMapping)
        this.verifyProcessorNotCalled()
    }

    @Test
    fun `should not process the controller`() {
        this.postProcessAndCheckReturnValue(InvalidSlashCommandController)
        this.verifyProcessorNotCalled()
    }

    private fun postProcessAndCheckReturnValue(value: Any) {
        expectThat(slashCommandControllerBeanPostProcessor.postProcessBeforeInitialization(value, VALID_STUB))
            .isEqualTo(value)
    }

    private fun verifyProcessorNotCalled() {
        verify(exactly = 0) {
            mappingProcessor.process(any())
        }
    }
}