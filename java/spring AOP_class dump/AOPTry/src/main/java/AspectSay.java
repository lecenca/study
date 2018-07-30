import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.io.IOException;

@Aspect
public class AspectSay {

    @Before("execution(* SayHello.sayHello(..))")
    public void beforeHello(){
        System.out.println("before hello");
    }
}
