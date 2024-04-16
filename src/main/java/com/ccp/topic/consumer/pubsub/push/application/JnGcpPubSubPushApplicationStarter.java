package com.ccp.topic.consumer.pubsub.push.application;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.implementations.db.bulk.elasticsearch.CcpElasticSerchDbBulk;
import com.ccp.implementations.db.dao.elasticsearch.CcpElasticSearchDao;
import com.ccp.implementations.db.query.elasticsearch.CcpElasticSearchQueryExecutor;
import com.ccp.implementations.db.utils.elasticsearch.CcpElasticSearchDbRequest;
import com.ccp.implementations.email.sendgrid.CcpSendGridEmailSender;
import com.ccp.implementations.file.bucket.gcp.CcpGcpFileBucket;
import com.ccp.implementations.http.apache.mime.CcpApacheMimeHttp;
import com.ccp.implementations.instant.messenger.telegram.CcpTelegramInstantMessenger;
import com.ccp.implementations.json.gson.CcpGsonJsonHandler;

@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {
		JnGcpPubSubConsumerController.class,
		JnGcpPubSubPushExceptionHandler.class
})
@SpringBootApplication
public class JnGcpPubSubPushApplicationStarter {

	public static void main(String[] args) {
		CcpDependencyInjection.loadAllDependencies( 
				new CcpGcpFileBucket(),  
				new CcpApacheMimeHttp(),
				new CcpGsonJsonHandler(),
				new CcpElasticSearchDao(),
				new CcpElasticSerchDbBulk(),
				new CcpSendGridEmailSender(),
				new CcpElasticSearchDbRequest(),
				new CcpTelegramInstantMessenger(),
				new CcpElasticSearchQueryExecutor()
				);
		SpringApplication.run(JnGcpPubSubPushApplicationStarter.class, args);

		
		
	}
	
}
