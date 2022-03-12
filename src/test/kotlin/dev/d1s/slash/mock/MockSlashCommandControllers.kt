package dev.d1s.slash.mock

import dev.d1s.slash.annotation.Option
import dev.d1s.slash.annotation.SlashCommandController
import dev.d1s.slash.annotation.SlashCommandMapping
import dev.d1s.slash.domain.SlashCommandDefinition
import dev.d1s.teabag.testing.constant.VALID_STUB
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import strikt.api.expectThat
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.isTrue
import java.util.concurrent.atomic.AtomicBoolean

@Configuration
internal class ControllerConfiguration {

    @Bean
    fun validSlashCommandController() = ValidSlashCommandController
}

@SlashCommandController
internal object ValidSlashCommandController {

    private var executionContext: SlashCommandExecutionContext? = null
    private var customizerCalled = AtomicBoolean(false)

    @SlashCommandMapping(VALID_STUB, VALID_STUB, VALID_STUB, customizer = "customizer")
    fun testRequiredOption(@Option(VALID_STUB, VALID_STUB) testOption: String, testInjectableParameter: Any?): Any {
        executionContext = SlashCommandExecutionContext(
            testOption,
            testInjectableParameter
        )

        return Any()
    }

    fun customizer(definition: SlashCommandDefinition) {
        customizerCalled.set(true)
    }

    fun verifyCustomizerCall() {
        expectThat(customizerCalled.getAndSet(false)).isTrue()
    }

    @SlashCommandMapping("_$VALID_STUB", VALID_STUB)
    fun testOptionalOption(
        @Option(VALID_STUB, VALID_STUB, required = false) testOption: String?,
        testInjectableParameter: Any?
    ): Any {
        executionContext = SlashCommandExecutionContext(
            testOption,
            testInjectableParameter
        )

        return Any()
    }

    fun verifyCall(clearContext: Boolean = true) = expectThat(executionContext).isNotNull()
        .clearExecutionContext(clearContext)

    fun verifyNotCalled(clearContext: Boolean = true) = expectThat(executionContext).isNull()
        .clearExecutionContext(clearContext)

    fun getExecutionContext(clearContext: Boolean = true) = executionContext
        .clearExecutionContext(clearContext)

    private fun <T> T.clearExecutionContext(really: Boolean): T = also {
        if (really) {
            executionContext = null
        }
    }
}

@SlashCommandController
internal object SlashCommandControllerWithNoMapping {

    fun test() {
    }
}

internal object InvalidSlashCommandController {

    @SlashCommandMapping(VALID_STUB, VALID_STUB)
    fun test() {
    }
}

internal class SlashCommandExecutionContext(
    val testOption: String?,
    val testInjectableParameter: Any?
)