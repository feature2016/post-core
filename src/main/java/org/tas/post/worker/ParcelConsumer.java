package org.tas.post.worker;


import lombok.extern.slf4j.Slf4j;
import org.tas.post.model.Parcel;
import org.tas.post.queue.ParcelQueue.State;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 邮差
 *
 * @author susan
 * @date 2016-7-11
 */
@Slf4j
public class ParcelConsumer {

    private Map<String, Recipient> recipients = new HashMap<>();

    /**
     * 记住收件人地址
     */
    public ParcelConsumer regist(String id, Recipient recipient) {
        recipients.put(id, recipient);
        log.info("注册收件人{}", id);
        return this;
    }

    /**
     * 投递
     */
    public void accept(Parcel[] parcels) {
        try {
            // 分拣
            Map<String, List<Parcel>> pipeLine = Arrays.stream(parcels)
                    .filter(parcel -> parcel != null && parcel.getSendTo() != null)
                    .collect(Collectors.groupingBy(Parcel::getSendTo));
            pipeLine.forEach((sendTo, list) -> {
                Optional.ofNullable(recipients.get(sendTo)).ifPresent(rec -> {
                    rec.take(list);
                    if (log.isDebugEnabled()) {
                        log.debug("投递到收件人{}", sendTo);
                    }
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 响应邮箱事件
     */
    public void notifyEvent(Parcel[] parcels, State state) {
        if (state == State.IS_TIME_NOW || state == State.QUEUE_FULL) {
            accept(parcels);
        }
    }
}
