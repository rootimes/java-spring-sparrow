package sparrow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import sparrow.MessageService;

@Configuration
@Import(JPAMySqlConfig.class)
@ComponentScan(basePackages = "sparrow", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {
                Controller.class,
                RestController.class,
                ControllerAdvice.class,
                Configuration.class
        })
})
@EnableAspectJAutoProxy
public class AppConfig {
    @Bean
    public MessageService messageService() {
        return new MessageService();
    }
}
