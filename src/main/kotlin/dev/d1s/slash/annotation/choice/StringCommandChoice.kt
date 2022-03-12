package dev.d1s.slash.annotation.choice

@Retention(AnnotationRetention.RUNTIME)
public annotation class StringCommandChoice(
    val name: String,
    val value: String
)
