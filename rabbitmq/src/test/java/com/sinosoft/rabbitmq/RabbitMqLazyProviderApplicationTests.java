package com.sinosoft.rabbitmq;

import com.sinosoft.rabbitmq.delay.MessageProvider;
import com.sinosoft.rabbitmq.delay.QueueEnum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Auther: zouren
 * @Date: 2019/5/20 16:29
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitmqApplication.class)
public class RabbitMqLazyProviderApplicationTests {
    /**
     * 消息队列提供者
     */
    @Autowired
    private MessageProvider messageProvider;

    /**
     * 测试延迟消息消费
     */
    @Test
    public void testLazy() {
        int delayTimes = 100000;
        int delayTimesIn = -100;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (int i = 0;i<10;i++){
            // 测试延迟10秒  10000
            LocalDateTime now = LocalDateTime.now() ;
            String nowStr = now.format(dateTimeFormatter);
            String delayTimesStr = now.minusSeconds(delayTimesIn).format(dateTimeFormatter);
            messageProvider.sendMessage("测试延迟消费,写入时间：" + nowStr+"   delayTimesStr="+delayTimesStr+"  delayTimes="+delayTimes,
                    QueueEnum.MESSAGE_TTL_QUEUE.getExchange(),
                    QueueEnum.MESSAGE_TTL_QUEUE.getRouteKey(),
                    delayTimes);
            delayTimes =  delayTimes-10000;
            delayTimesIn = delayTimesIn+10;
        }

    }
    @Test
    public void testLazy1() {
        int delayTimes = 100000;
        int delayTimesIn = -100;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime now = LocalDateTime.now() ;
            String nowStr = now.format(dateTimeFormatter);
            String delayTimesStr = now.minusSeconds(delayTimesIn).format(dateTimeFormatter);
            messageProvider.sendMessage("测试延迟消费,写入时间：" + nowStr+"   delayTimesStr="+delayTimesStr+"  delayTimes="+delayTimes,
                    QueueEnum.MESSAGE_TTL_QUEUE.getExchange(),
                    QueueEnum.MESSAGE_TTL_QUEUE.getRouteKey(),
                    delayTimes);
    }


    @Test
    public void testLazy2_1() {
        int delayTimes = 100000;
        int delayTimesIn = -100;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime now = LocalDateTime.now() ;
        String nowStr = now.format(dateTimeFormatter);
        String delayTimesStr = now.minusSeconds(delayTimesIn).format(dateTimeFormatter);
        messageProvider.sendMsg("测试延迟消费,写入时间：" + nowStr+"   delayTimesStr="+delayTimesStr+"  delayTimes="+delayTimes,
                delayTimes);
    }
    @Test
    public void testLazy2_2() {
        int delayTimes = 100000;
        int delayTimesIn = -100;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (int i = 0;i<10;i++){
            // 测试延迟10秒  10000
            LocalDateTime now = LocalDateTime.now() ;
            String nowStr = now.format(dateTimeFormatter);
            String delayTimesStr = now.minusSeconds(delayTimesIn).format(dateTimeFormatter);
            messageProvider.sendMsg("测试延迟消费,写入时间：" + nowStr+"   delayTimesStr="+delayTimesStr+"  delayTimes="+delayTimes,
                    delayTimes);
            delayTimes =  delayTimes-10000;
            delayTimesIn = delayTimesIn+10;
        }
    }
}
