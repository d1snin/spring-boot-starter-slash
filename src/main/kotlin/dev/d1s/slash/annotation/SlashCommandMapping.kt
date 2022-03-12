package dev.d1s.slash.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
public annotation class SlashCommandMapping(
    val name: String,
    val description: String,
    val guildId: String = "",
    val enabledByDefault: Boolean = true,
    val customizer: String = ""
)
