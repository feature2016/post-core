package org.tas.post.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件装箱
 *
 * @author susan
 * @date 2016-7-9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcel {

	/**
	 * 收件人
	 */
	private String sendTo;
	/**
	 * 标签
	 */
	private String tags;
	/**
	 * 附加消息
	 */
	private String msg;
	/**
	 * 投递包裹
	 */
	private Object payLoad;
	/**
	 * 发件时间
	 */
	private Long createTime;
}
