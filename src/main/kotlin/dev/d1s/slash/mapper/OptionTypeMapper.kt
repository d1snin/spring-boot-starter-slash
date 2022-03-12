package dev.d1s.slash.mapper

import dev.d1s.slash.domain.OptionTypeDefinition

public interface OptionTypeMapper {

    public fun mapType(type: Class<*>): OptionTypeDefinition
}
