package uno.d1s.slash.executor

import uno.d1s.slash.domain.execution.InjectableOption
import uno.d1s.slash.domain.execution.InjectableParameter
import uno.d1s.slash.processor.ReturnValueProcessor

public interface SlashCommandExecutor {

    public fun execute(
        commandName: String,
        injectableParameters: Set<InjectableParameter>,
        injectableOptions: Set<InjectableOption>,
        returnValueProcessor: ReturnValueProcessor
    )
}
