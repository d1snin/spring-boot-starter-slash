[![](https://jitpack.io/v/d1snin/spring-boot-starter-slash.svg)](https://jitpack.io/#d1snin/spring-boot-starter-slash)

# spring-boot-starter-slash
A starter that makes it easier for Spring Boot developers to 
create slash-command bots for Discord in an MVC-like style (just like `@Controller`).
The starter is not tied to a specific API implementation for the JVM.

### Installation
```kotlin
repositories {
    maven(url = "https://jitpack.io")
}

implementation("uno.d1s:spring-boot-starter-slash:$slashStarterVersion")
```

### Example usage
```java
@SlashCommandController
public final class TestController {

    private final HelloService helloService;
    
    @Autowired
    public TestController(final HelloService helloService) {
        this.helloService = helloService;
    }
    
    @SlashCommandMapping(name = "hello", description = "Say hello.")
    public String hello(@Option(name = "name", description = "Your name.") String name) {
        return helloService.sayHello(name)
    }
}
```

The bot will reply with plain message in the example above.

### Supported API implementations
- [x] [`JDA`](https://github.com/DV8FromTheWorld/JDA)

There is an autoconfiguration for each implementation, all you need to do is add the necessary dependency to your project.
