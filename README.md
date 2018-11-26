# post-core
实现邮递过程的生产者-消费者模型，把大规模生产者消息归并到少数几个消费者进程去处理



## 使用例子
```
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
      			log.info(JSON.toJSONString(parcels));
      		};
      
      		// 任命一个邮差，他负责给tas等人所在街道送信
      		ParcelConsumer postMan = new ParcelConsumer();
      		postMan.regist("tas", tas);
      
      		// 邮局开始营业, 邮差定期检查邮箱, 预估邮差每次搬运100封邮件
      		ParcelFactory.buildProducer(postMan, 100).deliver(parcel);
      
      		Thread.sleep(3000L);
      	}
 ```
