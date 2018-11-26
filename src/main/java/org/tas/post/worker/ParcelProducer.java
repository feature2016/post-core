package org.tas.post.worker;


import lombok.Getter;
import org.tas.post.model.Parcel;
import org.tas.post.queue.ParcelQueue;

/**
 * 发件人
 *
 * @author susan
 * @date 2016-7-11
 */
@Getter
public class ParcelProducer {

	private ParcelQueue parcelQueue;

	public ParcelProducer(ParcelQueue parcelQueue) {
		this.parcelQueue = parcelQueue;
	}

	/**
	 * 投递
	 */
	public void deliver(Parcel parcel) {
		try {
			parcelQueue.saveAdd(parcel);
		} catch (Exception e) {

		}
	}
}
