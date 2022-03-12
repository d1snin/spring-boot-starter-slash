package dev.d1s.slash.annotation.choice

@Retention(AnnotationRetention.RUNTIME)
public annotation class LongCommandChoice(
    val name: String,
    val value: Long
)
