package service;

import dao.WalletMapper;
import entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


//给模块添加缓存功能
//关键点1：spring-cache 给模块添加缓存功能的方式是 spring-aop（底层实现是代理）
//关键点2：缓存功能的具体实现在 cacheManager中，所以配置文件要配置cacheManager
@Service
public class PayService {

    @Autowired
    private WalletMapper walletMapper;


    @Cacheable(value="showMoney",key = "#name")
    public Wallet showMoney(String name){
        System.out.println("showMoney() query "+name);
        return walletMapper.selectByPrimaryKey(name);
    }

    //payer向payee转钱，金额为money
    @CacheEvict(value = {"showMoney"},allEntries = true)
    public void pay(String payer, String payee, int money){
        Wallet wallet = walletMapper.selectByPrimaryKey(payer);
        if(wallet.getMoney()<money)
            return;
        wallet.setMoney(wallet.getMoney()-money);
        walletMapper.updateByPrimaryKey(wallet);

        wallet = walletMapper.selectByPrimaryKey(payee);
        wallet.setMoney(wallet.getMoney()+money);
        walletMapper.updateByPrimaryKey(wallet);
    }
}
