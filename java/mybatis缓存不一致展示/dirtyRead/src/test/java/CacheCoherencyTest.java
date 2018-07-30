import dao.WalletMapper;
import entity.Wallet;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-dao.xml"})
public class CacheCoherencyTest {

    @Autowired
    @Qualifier(value = "sqlSessionFactory")
    SqlSessionFactory sqlSessionFactory;

    @Autowired
    @Qualifier(value = "sqlSessionFactory02")
    SqlSessionFactory sqlSessionFactory02;

    //mybatis开启一级缓存时会有缓存不一致的问题。
    @Test
    public void test001Conherency(){

        //开启自动commit，默认一级缓存，两个session的缓存相互独立
        SqlSession session01 = sqlSessionFactory.openSession(true);
        SqlSession session02 = sqlSessionFactory.openSession(true);

        WalletMapper walletMapper01 = session01.getMapper(WalletMapper.class);
        WalletMapper walletMapper02 = session02.getMapper(WalletMapper.class);

        //两者都读取主键为"AAA"的那一行的内容，会得到一样的结果。并且，在各自的session中缓存了结果，下次做
        //同样的查询将优先从缓存中取结果
        Wallet wallet01 = walletMapper01.selectByPrimaryKey("AAA");
        System.out.println("walletMapper01 get {AAA,"+wallet01.getMoney()+"}");

        Wallet wallet02 = walletMapper02.selectByPrimaryKey("AAA");
        System.out.println("walletMapper02 get {AAA,"+wallet02.getMoney()+"}");

        //walletMapper更改主键为"AAA"的那一行的数据，session02中的缓存失效，walletMapper02
        //再次读取主键为"AAA"的那一行的内容时，将从数据库读取，并重新存入缓存
        wallet02.setMoney(wallet02.getMoney()+100);
        walletMapper02.updateByPrimaryKey(wallet02);

        //由于walletMapper01从缓存中取结果，walletMapper02从数据库取并重新存入缓存
        //所以两者显示结果不一致。这种情况会出现“一级缓存不一致”。
        wallet01 = walletMapper01.selectByPrimaryKey("AAA");
        System.out.println("walletMapper01 get {AAA,"+wallet01.getMoney()+"}");

        wallet02 = walletMapper02.selectByPrimaryKey("AAA");
        System.out.println("walletMapper02 get {AAA,"+wallet02.getMoney()+"}");
    }


    @Test
    public void test002Conherency(){

        //sqlSessionFactory02禁用一级缓存，就不会发生一级缓存不一致的现象了

        SqlSession session01 = sqlSessionFactory02.openSession(true);
        SqlSession session02 = sqlSessionFactory02.openSession(true);

        WalletMapper walletMapper01 = session01.getMapper(WalletMapper.class);
        WalletMapper walletMapper02 = session02.getMapper(WalletMapper.class);

        Wallet wallet01 = walletMapper01.selectByPrimaryKey("AAA");
        System.out.println("walletMapper01 get {AAA,"+wallet01.getMoney()+"}");

        Wallet wallet02 = walletMapper02.selectByPrimaryKey("AAA");
        System.out.println("walletMapper02 get {AAA,"+wallet02.getMoney()+"}");

        wallet02.setMoney(wallet02.getMoney()+100);
        walletMapper02.updateByPrimaryKey(wallet02);

        wallet01 = walletMapper01.selectByPrimaryKey("AAA");
        System.out.println("walletMapper01 get {AAA,"+wallet01.getMoney()+"}");

        wallet02 = walletMapper02.selectByPrimaryKey("AAA");
        System.out.println("walletMapper02 get {AAA,"+wallet02.getMoney()+"}");
    }
}
