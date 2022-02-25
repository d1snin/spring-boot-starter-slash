package uno.d1s.slash.domain.execution

public class InjectableOption(
    public val name: String
) {
    public var obj: Any? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InjectableOption

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
