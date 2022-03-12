package dev.d1s.slash.util

import dev.d1s.slash.annotation.Option
import dev.d1s.slash.annotation.SlashCommandMapping
import org.apache.commons.lang3.reflect.MethodUtils
import org.springframework.core.annotation.AnnotationUtils
import java.lang.reflect.Method
import java.lang.reflect.Parameter

internal fun Parameter.findOptionAnnotation(): Option? {
    if (isOption()) {
        return getOption()
    }

    val currentMethod = declaringExecutable as Method

    MethodUtils.getMethodsListWithAnnotation(
        declaringExecutable.declaringClass,
        SlashCommandMapping::class.java,
        true,
        false
    ).filter {
        it.name == currentMethod.name
                && it.parameterTypeNames == currentMethod.parameterTypeNames
    }.forEach { method ->
        val param = method.parameters.first {
            it.name == name
                    && it.type == type
        }

        if (param.isOption()) {
            return param.getOption()
        }
    }

    return null
}

private fun Parameter.isOption(): Boolean = isAnnotationPresent(Option::class.java)
private fun Parameter.getOption() = AnnotationUtils.findAnnotation(this, Option::class.java)!!
private val Method.parameterTypeNames
    get() = parameterTypes.map {
        it.name
    }