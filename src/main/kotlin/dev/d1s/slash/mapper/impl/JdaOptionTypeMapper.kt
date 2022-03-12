package dev.d1s.slash.mapper.impl

import dev.d1s.slash.domain.OptionTypeDefinition
import dev.d1s.slash.mapper.OptionTypeMapper
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.IMentionable
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User

internal class JdaOptionTypeMapper : OptionTypeMapper {

    override fun mapType(type: Class<*>): OptionTypeDefinition =
        when (type) {
            String::class.java -> OptionTypeDefinition.STRING
            Long::class.java -> OptionTypeDefinition.INTEGER
            Double::class.java -> OptionTypeDefinition.NUMBER
            Boolean::class.java -> OptionTypeDefinition.BOOLEAN
            User::class.java -> OptionTypeDefinition.USER
            GuildChannel::class.java -> OptionTypeDefinition.CHANNEL
            Role::class.java -> OptionTypeDefinition.ROLE
            IMentionable::class.java -> OptionTypeDefinition.MENTIONABLE
            else -> throw IllegalArgumentException("Type is not supported: $type")
        }
}