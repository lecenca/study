import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SpeakHandler implements InvocationHandler{
    private Speakable speaker;

    public SpeakHandler(Speakable speaker){
        this.speaker = speaker;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("do before");
        method.invoke(speaker,args);
        System.out.println("do after");
        return null;
    }
}
