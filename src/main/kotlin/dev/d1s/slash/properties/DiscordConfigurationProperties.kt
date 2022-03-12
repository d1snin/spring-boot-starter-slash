package dev.d1s.slash.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
@ConfigurationProperties(prefix = "discord")
public class DiscordConfigurationProperties(
    @NotBlank public var token: String? = null
)
