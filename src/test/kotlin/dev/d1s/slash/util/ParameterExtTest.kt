package dev.d1s.slash.util

import dev.d1s.slash.annotation.Option
import dev.d1s.slash.mock.ValidSlashCommandController
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.reflect.jvm.javaMethod

internal class ParameterExtTest {

    @Test
    fun `should get the option annotation`() {
        val parameter = ValidSlashCommandController::testRequiredOption.javaMethod!!.parameters[0]
        val option = parameter.getAnnotation(Option::class.java)

        expectThat(parameter.findOptionAnnotation()).isEqualTo(option)
    }
}