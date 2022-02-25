package uno.d1s.slash.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "discord")
public class DiscordConfigurationProperties(
    @NotBlank public var token: String? = null
)
