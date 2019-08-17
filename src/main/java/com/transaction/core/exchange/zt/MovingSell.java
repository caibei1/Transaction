package com.transaction.core.exchange.zt;

import com.transaction.core.exchange.pub.PubDeal;
import com.transaction.core.exchange.pub.RestTemplateStatic;
import com.transaction.core.exchange.pubinterface.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class MovingSell extends Thread {

    private Exchange client;
    private String sy1;
    private String sy2;
    private Lock lock;

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public Exchange getClient() {
        return client;
    }

    public void setClient(Exchange client) {
        this.client = client;
    }

    public String getSy1() {
        return sy1;
    }

    public void setSy1(String sy1) {
        this.sy1 = sy1;
    }

    public String getSy2() {
        return sy2;
    }

    public void setSy2(String sy2) {
        this.sy2 = sy2;
    }

    public MovingSell(Exchange client, String sy1, String sy2) {
        this.client = client;
        this.sy1 = sy1;
        this.sy2 = sy2;
    }


    RestTemplate restTemplate = RestTemplateStatic.restTemplate();
    Logger logger = LoggerFactory.getLogger(this.getClass());


    public void run(){
        while (true){
            try {
                PubDeal t = new PubDeal(client);
                double usdtcount = t.getFirstCount(sy1,sy2,"BUY");
                if (usdtcount == 0.0){
                    info("获取市场行情失败");
                }
                if(usdtcount > 5){
                    info("预计一轮后的usdt：" + usdtcount);
                }

            }catch (Exception e){
                logger.error("{}{}计算出错，{}",sy1,sy2,e.getMessage());
            }

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void info(String msg){
        logger.info("ZT,  " + sy1 + sy2 + ", 方式: BUY. " + msg);
    }

}