import entity.Wallet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.PayService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-dao.xml"})
public class CacheTest {

    @Autowired
    private PayService payService;

    @Test
    public void cache001Tset(){
        Wallet wallet = payService.showMoney("AAA");
        wallet = payService.showMoney("BBB");

        //由于使用了缓存，此次查询将不会查询数据库，
        //事实上连 showMoney() 函数体内的代码都不会执行。
        wallet = payService.showMoney("AAA");
    }

    @Test
    public void cache002Tset(){
        payService.showMoney("AAA");
        payService.showMoney("BBB");

        //由于使用了缓存，此次查询将不会查询数据库，
        //事实上连 showMoney() 函数体内的代码都不会执行。
        payService.showMoney("AAA");

        //pay()操作会清空缓存，接下来的showMoney()会去数据库进行查询
        payService.pay("AAA","BBB",0);

        payService.showMoney("AAA");
    }
}
