package dev.d1s.slash.listener

import com.ninjasquad.springmockk.MockkBean
import dev.d1s.slash.executor.SlashCommandExecutor
import io.mockk.mockk
import io.mockk.verify
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [JdaSlashCommandInteractionEventListener::class])
internal class JdaSlashCommandInteractionEventListenerTest {

    @Autowired
    private lateinit var jdaSlashCommandInteractionEventListener: JdaSlashCommandInteractionEventListener

    @MockkBean(relaxUnitFun = true)
    private lateinit var slashCommandExecutor: SlashCommandExecutor

    private val mockSlashCommandInteractionEvent = mockk<SlashCommandInteractionEvent>(relaxed = true)

    @Test
    fun `should run the execution`() {
        assertDoesNotThrow {
            jdaSlashCommandInteractionEventListener.onSlashCommandInteraction(mockSlashCommandInteractionEvent)
        }

        verify {
            slashCommandExecutor.execute(any(), any(), any(), any())
        }
    }
}