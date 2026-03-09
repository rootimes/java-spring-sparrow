package sparrow;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import sparrow.config.AppConfig;
import sparrow.entity.User;
import sparrow.repository.UserRepository;
import sparrow.service.MessageService;

public class App {
    public static void main(String[] args) {
        GenericApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        try {
            UserRepository userRepository = context.getBean(UserRepository.class);

            System.out.println("正在執行...");
            User newUser = new User();

            newUser.setPassword("password");
            newUser.setName("Tester");
            newUser.setEmail("test@sparrow.dev");
            newUser.setDescription("這是一個測試使用者");

            userRepository.save(newUser);
            System.out.println("使用者已儲存，ID 為: " + newUser.getId());

            List<User> users = userRepository.findAll();
            System.out.println("目前資料庫中的使用者總數: " + users.size());
            users.forEach(u -> System.out.println(" - " + u.getName() + " (" + u.getEmail() + ")"));

        } catch (Exception e) {
            System.err.println("發生錯誤:");
            e.printStackTrace();
        } finally {
            System.out.println("關閉 Spring 容器...");
        }

        MessageService messageService = context.getBean(MessageService.class);

        System.out.println(messageService.getMessage());

        context.close();
    }
}
