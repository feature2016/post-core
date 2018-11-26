package org.tas.post.queue;

import lombok.extern.slf4j.Slf4j;
import org.tas.post.model.Parcel;
import org.tas.post.worker.ParcelConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 邮箱
 *
 * @author susan
 * @date 2016-7-10
 */
@Slf4j
public class ParcelQueue extends ArrayBlockingQueue<Parcel> {

    private final List<ParcelConsumer> listenerList = new ArrayList<>();
    private Integer qLength;
    private ScheduledThreadPoolExecutor executorService;

    public ParcelQueue(int capacity, int qLength) {
        super(capacity);
        this.qLength = qLength;
        executorService = new ScheduledThreadPoolExecutor(1,
                new ThreadPoolExecutor.DiscardPolicy());
        executorService.scheduleAtFixedRate(() -> {
            notifyEvent(State.IS_TIME_NOW);
        }, 10, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 注册收件人
     */
    public void register(ParcelConsumer consumer) {
        listenerList.add(consumer);
    }

    /**
     * 事件
     */
    public void notifyEvent(State state) {
        listenerList.forEach(consumer -> consumer.notifyEvent(safeObtain(), state));
    }

    /**
     * 获取邮包
     */
    public Parcel[] safeObtain() {
        Parcel[] result = new Parcel[qLength];
        try {
            for (int i = 0; i < qLength; i++) {
                Parcel product = this.poll(1, TimeUnit.MILLISECONDS);
                if (product == null) {
                    break;
                }
                result[i] = product;
            }
            return result;
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * 装入邮箱
     */
    public void saveAdd(Parcel product) throws InterruptedException {
        if (!offer(product, 1, TimeUnit.MILLISECONDS)) {
            notifyEvent(State.QUEUE_FULL);
            put(product);
        }
    }

    /**
     * 状态
     */
    public enum State {
        QUEUE_FULL, IS_TIME_NOW
    }
}
