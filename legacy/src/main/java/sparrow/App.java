package sparrow;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class App {
  public static void main(String[] args) {
    GenericApplicationContext context = new AnnotationConfigApplicationContext(ProjectConfig.class);

    MessageService messageService = context.getBean(MessageService.class);

    System.out.println(messageService.getMessage());

    context.close();
  }
}
