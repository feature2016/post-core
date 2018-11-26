package org.tas.post.worker;

import java.util.List;
import org.tas.post.model.Parcel;

/**
 * 收件人
 */
public interface Recipient {

	/**
	 * 收信
	 */
	void take(List<Parcel> parcels);

}
