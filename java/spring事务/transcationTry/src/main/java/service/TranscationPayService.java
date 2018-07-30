package service;

import dao.WalletMapper;
import entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TranscationPayService {

    @Autowired
    private WalletMapper walletMapper;

    @Transactional
    public void pay() throws Exception {
        Wallet wallet = walletMapper.selectByPrimaryKey("AAA");
        System.out.println("A的钱包有"+wallet.getMoney()+"元");
        wallet.setMoney(wallet.getMoney()-200);
        walletMapper.updateByPrimaryKey(wallet);

        System.out.println("从A钱包里扣了200"+"，还剩"+wallet.getMoney());

        walletMapper.insert(wallet);//重复插入，抛出异常，触发回滚，A回到扣钱前的状态。


        wallet = walletMapper.selectByPrimaryKey("BBB");
        wallet.setMoney(wallet.getMoney()+200);
        walletMapper.updateByPrimaryKey(wallet);
    }
}
