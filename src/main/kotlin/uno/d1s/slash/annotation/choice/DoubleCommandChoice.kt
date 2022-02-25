package uno.d1s.slash.annotation.choice

@Retention(AnnotationRetention.RUNTIME)
public annotation class DoubleCommandChoice(
    val name: String,
    val value: Double
)
