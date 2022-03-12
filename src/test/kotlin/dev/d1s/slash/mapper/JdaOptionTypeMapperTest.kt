package dev.d1s.slash.mapper

import dev.d1s.slash.domain.OptionTypeDefinition
import dev.d1s.slash.mapper.impl.JdaOptionTypeMapper
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.IMentionable
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest
@ContextConfiguration(classes = [JdaOptionTypeMapper::class])
internal class JdaOptionTypeMapperTest {

    @Autowired
    private lateinit var jdaOptionTypeMapper: JdaOptionTypeMapper

    @Test
    fun `should map the type to OptionTypeDefinition`() {
        mapOf(
            String::class.java to OptionTypeDefinition.STRING,
            Long::class.java to OptionTypeDefinition.INTEGER,
            Double::class.java to OptionTypeDefinition.NUMBER,
            Boolean::class.java to OptionTypeDefinition.BOOLEAN,
            User::class.java to OptionTypeDefinition.USER,
            GuildChannel::class.java to OptionTypeDefinition.CHANNEL,
            Role::class.java to OptionTypeDefinition.ROLE,
            IMentionable::class.java to OptionTypeDefinition.MENTIONABLE
        ).forEach {
            expectThat(jdaOptionTypeMapper.mapType(it.key))
                .isEqualTo(it.value)
        }
    }

    @Test
    fun `should throw IllegalArgumentException if the type can not be mapped`() {
        assertThrows<IllegalArgumentException> {
            jdaOptionTypeMapper.mapType(Any::class.java)
        }
    }
}