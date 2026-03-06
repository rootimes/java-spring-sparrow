package sparrow.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
  @Before("execution(* sparrow.service.MessageService.getMessage(..))")
  public void logBeforeGetMessage() {
    System.out.println("LoggingAspect: Before executing getMessage()");
  }
}
