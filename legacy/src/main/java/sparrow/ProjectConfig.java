package sparrow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {
    @Bean
    public MessageService messageService() {
        return new MessageService();
    }
}
