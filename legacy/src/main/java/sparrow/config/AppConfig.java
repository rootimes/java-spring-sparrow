package sparrow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import sparrow.service.MessageService;

@Configuration
@ComponentScan(basePackages = "sparrow")
@EnableAspectJAutoProxy
public class AppConfig {
    @Bean
    public MessageService messageService() {
        return new MessageService();
    }
}
