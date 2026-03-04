package sparrow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "sparrow")
@EnableAspectJAutoProxy
public class ProjectConfig {
    @Bean
    public MessageService messageService() {
        return new MessageService();
    }
}
