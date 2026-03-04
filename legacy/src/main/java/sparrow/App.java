package sparrow;

import org.springframework.context.support.GenericApplicationContext;

public class App {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.refresh();

        System.out.println("Spring 容器啟動成功！目前的 Bean 數量：" + context.getBeanDefinitionCount());

        context.close();
    }
}
