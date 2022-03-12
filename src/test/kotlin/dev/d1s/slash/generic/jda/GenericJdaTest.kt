package dev.d1s.slash.generic.jda

import dev.d1s.teabag.testing.constant.INVALID_STUB
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.sharding.ShardManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal class GenericJdaTest {

    private val mockJda = mockk<JDA>(relaxed = true)
    private val mockShardManager = mockk<ShardManager>(relaxed = true)
    private val commandDataSet = setOf(mockk<CommandData>(relaxed = true))

    @BeforeEach
    fun setup() {
        every {
            mockJda.getGuildById(INVALID_STUB)
        } returns null

        every {
            mockShardManager.getGuildById(INVALID_STUB)
        } returns null

        every {
            mockShardManager.shards
        } returns listOf(mockJda)
    }

    @Test
    fun `should throw IllegalStateException if the provided API is not supported`() {
        assertThrows<IllegalStateException> {
            GenericJda(Any())
        }
    }

    @Test
    fun `should await readiness for each shard when using ShardManager`() {
        assertDoesNotThrow {
            GenericJda(mockShardManager)
        }

        verify {
            mockJda.awaitReady()
        }
    }

    @Test
    fun `should return guild by id when using JDA`() {
        assertDoesNotThrow {
            GenericJda(mockJda).getGuildById(VALID_STUB)
        }

        verify {
            mockJda.getGuildById(VALID_STUB)
        }
    }

    @Test
    fun `should return guild by id when using ShardManager`() {
        assertDoesNotThrow {
            GenericJda(mockShardManager).getGuildById(VALID_STUB)
        }

        verify {
            mockShardManager.getGuildById(VALID_STUB)
        }
    }

    @Test
    fun `should throw IllegalArgumentException if the guild was not found when using both APIs`() {
        assertThrows<IllegalArgumentException> {
            GenericJda(mockJda).getGuildById(INVALID_STUB)
        }

        verify {
            mockJda.getGuildById(INVALID_STUB)
        }

        assertThrows<IllegalArgumentException> {
            GenericJda(mockShardManager).getGuildById(INVALID_STUB)
        }

        verify {
            mockShardManager.getGuildById(INVALID_STUB)
        }
    }

    @Test
    fun `should upsert commands when using JDA`() {
        assertDoesNotThrow {
            GenericJda(mockJda).upsertCommands(commandDataSet)
        }

        this.verifyCommandUploading()
    }

    @Test
    fun `should upsert commands when using ShardManager`() {
        assertDoesNotThrow {
            GenericJda(mockShardManager).upsertCommands(commandDataSet)
        }

        this.verifyCommandUploading()
    }

    private fun verifyCommandUploading() {
        verify {
            mockJda.updateCommands().addCommands(commandDataSet).queue()
        }
    }
}