package dev.d1s.slash.util

import dev.d1s.slash.annotation.SlashCommandMapping
import org.springframework.core.annotation.AnnotationUtils
import java.lang.reflect.Method

internal fun Method.findSlashCommandMappingAnnotation() =
    AnnotationUtils.findAnnotation(this, SlashCommandMapping::class.java)