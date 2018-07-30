
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class SayHelloTest {


    //展示了最基本的spring AOP操作
    //此外，sayHello对象的class文件已经dump出来，可以进入 com 和 org 文件夹查看spring AOP生成的代理类
    @Test
    public void say(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring-cfg.xml");
        SayHello sayHello = (SayHello)ac.getBean("sayHello");

        sayHello.sayHello();
        System.out.println(sayHello.getClass().getName());

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
