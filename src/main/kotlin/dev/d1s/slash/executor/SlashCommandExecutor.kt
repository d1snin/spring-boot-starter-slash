package dev.d1s.slash.executor

import dev.d1s.slash.domain.execution.InjectableOption
import dev.d1s.slash.domain.execution.InjectableParameter
import dev.d1s.slash.processor.ReturnValueProcessor

public interface SlashCommandExecutor {

    public fun execute(
        commandName: String,
        injectableParameters: Set<InjectableParameter>,
        injectableOptions: Set<InjectableOption>,
        returnValueProcessor: ReturnValueProcessor
    )
}
