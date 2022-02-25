package uno.d1s.slash.mapper

import uno.d1s.slash.domain.OptionTypeDefinition

public interface OptionTypeMapper {

    public fun mapType(type: Class<*>): OptionTypeDefinition
}
