import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        AAA aaa = new AAA();
        BBB bbb = new BBB();
        Speakable aaaProxy = (Speakable) Proxy.newProxyInstance(
                AAA.class.getClassLoader(),
                new Class[]{Speakable.class},
                new SpeakHandler(aaa));
        Speakable bbbProxy = (Speakable) Proxy.newProxyInstance(
                BBB.class.getClassLoader(),
                new Class[]{Speakable.class},
                new SpeakHandler(bbb));
        aaaProxy.speak();
        bbbProxy.speak();
    }
}
