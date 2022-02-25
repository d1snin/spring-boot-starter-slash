package uno.d1s.slash.domain.execution

public data class InjectableParameter(
    public val type: Class<*>
) {
    public var obj: Any? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InjectableParameter

        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }

    public companion object {
        public inline fun <reified T> of(obj: T): InjectableParameter = InjectableParameter(T::class.java).apply {
            this.obj = obj
        }
    }
}
