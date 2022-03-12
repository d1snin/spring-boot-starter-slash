package dev.d1s.slash.registry

import dev.d1s.slash.mock.mockSlashCommandExecution
import dev.d1s.slash.registry.impl.SlashCommandExecutionRegistryImpl
import dev.d1s.slash.testUtil.setMockSlashCommandDefinition
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = [SlashCommandExecutionRegistryImpl::class])
internal class SlashCommandExecutionRegistryImplTest {

    @Autowired
    private val slashCommandExecutionRegistryImpl =
        SlashCommandExecutionRegistryImpl()

    private val execution = spyk(mockSlashCommandExecution(true).setMockSlashCommandDefinition())

    @BeforeAll
    fun setup() {
        slashCommandExecutionRegistryImpl += execution
    }

    @Test
    fun `should add the execution`() {
        assertDoesNotThrow {
            slashCommandExecutionRegistryImpl += mockSlashCommandExecution(false)
                .setMockSlashCommandDefinition()
        }
    }

    @Test
    fun `should throw IllegalArgumentException if the execution already exists`() {
        assertThrows<IllegalArgumentException> {
            slashCommandExecutionRegistryImpl += execution
        }
    }

    @Test
    fun `should get the execution with cloning`() {
        assertDoesNotThrow {
            slashCommandExecutionRegistryImpl[VALID_STUB, true]
        }

        verify {
            execution.clone()
        }
    }

    @Test
    fun `should get the execution with no cloning`() {
        assertDoesNotThrow {
            slashCommandExecutionRegistryImpl[VALID_STUB, false]
        }

        verify(exactly = 0) {
            execution.clone()
        }
    }
}