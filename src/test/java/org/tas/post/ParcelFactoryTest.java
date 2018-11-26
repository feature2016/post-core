package org.tas.post;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.tas.post.model.Parcel;
import org.tas.post.worker.ParcelConsumer;
import org.tas.post.worker.ParcelProducer;
import org.tas.post.worker.Recipient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试用例
 *
 * @author joseph
 * @date 2016-11-11
 */
@Slf4j
public class ParcelFactoryTest {

    /**
     * susan的情书
     */
    @Test
    public void buildProducer() throws InterruptedException {
        //  susan有一篇稿件装入封信
        Parcel parcel = Parcel.builder()
                .sendTo("tas")
                .tags("secret")
                .msg("邮差哥哥尽快送达呀")
                .payLoad("dear:\n   I see you!\n yours sussan")
                .createTime(System.currentTimeMillis())
                .build();

        // 定义tas收到邮件的拆箱动作
        Recipient tas = (parcels) -> {
            // 收到稿件,tas心情迭代
            log.info("收到邮件　{}", JSON.toJSONString(parcels));
        };

        // 任命一个邮差，他负责给tas等人所在街道送信
        ParcelConsumer postMan = new ParcelConsumer();
        postMan.regist("tas", tas);

        // 邮局开始营业, 邮差定期检查邮箱, 预估邮差每次搬运100封邮件
        ParcelFactory.buildProducer(postMan, 100).deliver(parcel);

        Thread.sleep(3000L);
    }


    @Test
    public void pressTest() throws InterruptedException {
        final AtomicInteger send = new AtomicInteger(0);
        final AtomicInteger receive = new AtomicInteger(0);

        ParcelConsumer parcelConsumer = new ParcelConsumer().regist("tas", (parcels) -> {
//            log.info("Receive {}", JSON.toJSONString(parcels));
            receive.addAndGet(parcels.size());
        });

        final ParcelProducer producer = ParcelFactory.buildProducer(parcelConsumer, 1024);

        int threads = 55;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        while (send.incrementAndGet() > 0) {
                            producer.deliver(
                                    Parcel.builder()
                                            .sendTo("tas")
                                            .payLoad(System.currentTimeMillis())
                                            .createTime(System.currentTimeMillis())
                                            .build()
                            );
                        }
                    } catch (Exception e) {
                        log.error("put error!", e);
                    }
                }
            });
        }

        Thread.sleep(10000L);
        log.info("send {} receive {}", send, receive);
    }
}