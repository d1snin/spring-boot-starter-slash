package uno.d1s.slash.domain

public data class CommandChoiceDefinition(
    val name: String,
    val value: Any
) {
    init {
        if (value !is String && value !is Double && value !is Long && value !is Int) {
            throw IllegalStateException("Command choice value should be String, Double, Long or Integer.")
        }
    }
}
