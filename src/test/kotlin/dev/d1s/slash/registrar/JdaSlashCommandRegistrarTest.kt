package dev.d1s.slash.registrar

import com.ninjasquad.springmockk.MockkBean
import dev.d1s.slash.generic.jda.GenericJda
import dev.d1s.slash.mock.mockSlashCommandDefinition
import dev.d1s.slash.registrar.impl.JdaSlashCommandRegistrar
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [JdaSlashCommandRegistrar::class])
internal class JdaSlashCommandRegistrarTest {

    @Autowired
    private lateinit var jdaSlashCommandRegistrar: JdaSlashCommandRegistrar

    @MockkBean(relaxed = true)
    private lateinit var genericJda: GenericJda<*>

    @Test
    fun `should register all commands`() {
        assertDoesNotThrow {
            jdaSlashCommandRegistrar.registerAll(setOf(mockSlashCommandDefinition))
        }

        verify {
            genericJda.getGuildById(VALID_STUB).upsertCommand(any())
        }
    }
}