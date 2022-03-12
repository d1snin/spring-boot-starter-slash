package dev.d1s.slash.mock

import dev.d1s.slash.domain.execution.SlashCommandExecution
import dev.d1s.slash.util.findSlashCommandMappingAnnotation
import kotlin.reflect.jvm.javaMethod

internal fun mockSlashCommandExecution(requiredOption: Boolean): SlashCommandExecution {
    val testRequiredOption = ValidSlashCommandController::testRequiredOption.javaMethod!!
    val testOptionalOption = ValidSlashCommandController::testOptionalOption.javaMethod!!
    val option = if (requiredOption) {
        testRequiredOption
    } else {
        testOptionalOption
    }

    return SlashCommandExecution(
        option,
        ValidSlashCommandController,
        option.findSlashCommandMappingAnnotation()!!
    )
}