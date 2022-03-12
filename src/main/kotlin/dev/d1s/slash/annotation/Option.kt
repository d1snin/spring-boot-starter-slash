package dev.d1s.slash.annotation

import dev.d1s.slash.annotation.choice.DoubleCommandChoice
import dev.d1s.slash.annotation.choice.LongCommandChoice
import dev.d1s.slash.annotation.choice.StringCommandChoice
import dev.d1s.slash.domain.ChannelTypeDefinition

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
public annotation class Option(
    val name: String,
    val description: String,
    val required: Boolean = true,
    val autoComplete: Boolean = false,
    val channelTypes: Array<ChannelTypeDefinition> = [],
    val minValue: Long = -1,
    val maxValue: Long = -1,
    val doubleChoices: Array<DoubleCommandChoice> = [],
    val longChoices: Array<LongCommandChoice> = [],
    val stringChoices: Array<StringCommandChoice> = []
)
