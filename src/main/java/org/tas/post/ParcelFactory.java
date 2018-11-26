package org.tas.post;


import lombok.extern.slf4j.Slf4j;
import org.tas.post.queue.ParcelQueue;
import org.tas.post.worker.ParcelConsumer;
import org.tas.post.worker.ParcelProducer;

/**
 * 邮政中心
 *
 * @author susan
 * @date 2016-7-11
 */
@Slf4j
public class ParcelFactory {

	/**
	 * 建造生产者
	 */
	public static ParcelProducer buildProducer(ParcelConsumer comsumer, Integer qLength) {
		ParcelQueue parcelQueue = new ParcelQueue(qLength < 10 ? qLength * 20 : qLength * 5,
			qLength);
		ParcelProducer parcelProducer = new ParcelProducer(parcelQueue);
		parcelQueue.register(comsumer);

		log.info("邮局构建, 邮箱长度为{}", qLength);
		return parcelProducer;
	}


}
