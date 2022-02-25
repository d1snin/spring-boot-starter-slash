package uno.d1s.slash.processor

public interface ReturnValueProcessor {

    public fun processValue(any: Any)

    public fun supportedTypes(): Set<Class<*>>
}