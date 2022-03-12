package dev.d1s.slash.util

import dev.d1s.slash.annotation.SlashCommandMapping
import dev.d1s.slash.mock.ValidSlashCommandController
import dev.d1s.slash.mock.mockSlashCommandExecution
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.core.annotation.AnnotationUtils
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.reflect.jvm.javaMethod

internal class MethodExtTest {

    @Test
    fun `should find the command mapping annotation`() {
        val method = ValidSlashCommandController::testRequiredOption.javaMethod!!
        val mapping = mockSlashCommandExecution(true).mapping

        mockkStatic(AnnotationUtils::class) {
            every {
                AnnotationUtils.findAnnotation(method, SlashCommandMapping::class.java)
            } returns mapping

            expectThat(method.findSlashCommandMappingAnnotation()).isEqualTo(mapping)

            verify {
                AnnotationUtils.findAnnotation(method, SlashCommandMapping::class.java)
            }
        }
    }
}