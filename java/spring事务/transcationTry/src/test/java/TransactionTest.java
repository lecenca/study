import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.NormalPayService;
import service.TranscationPayService;
import service.WrongPayService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-dao.xml"})
public class TransactionTest {

    @Autowired
    private WrongPayService wrongPayService;

    @Autowired
    private NormalPayService normalPayService;

    @Autowired
    private TranscationPayService transcationPayService;


    @Test
    public void normalPay(){
        normalPayService.pay();
    }

    @Test
    public void wrongPay(){
        try {
            wrongPayService.pay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void transcationPay(){
        try {
            transcationPayService.pay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
