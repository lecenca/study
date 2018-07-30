package service;

import dao.WalletMapper;
import entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalPayService {

    @Autowired
    private WalletMapper walletMapper;

    public void pay() {
        Wallet wallet = walletMapper.selectByPrimaryKey("AAA");
        wallet.setMoney(wallet.getMoney()-200);
        walletMapper.updateByPrimaryKey(wallet);

        wallet = walletMapper.selectByPrimaryKey("BBB");
        wallet.setMoney(wallet.getMoney()+200);
        walletMapper.updateByPrimaryKey(wallet);
    }
}
