package com.ccp.topic.consumer.pubsub.push.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.implementations.db.bulk.elasticsearch.Bulk;
import com.ccp.implementations.db.dao.elasticsearch.Dao;
import com.ccp.implementations.db.utils.elasticsearch.DbUtils;
import com.ccp.implementations.db.utils.elasticsearch.Query;
import com.ccp.implementations.emails.sendgrid.Email;
import com.ccp.implementations.file.bucket.gcp.FileBucket;
import com.ccp.implementations.http.apache.mime.Http;
import com.ccp.implementations.instant.messenger.telegram.InstantMessenger;
import com.ccp.implementations.text.extractor.apache.tika.JsonHandler;
import com.ccp.implementations.text.extractor.apache.tika.TextExtractor;
import com.ccp.topic.consumer.pubsub.push.controller.PubSubConsumerController;
import com.ccp.topic.consumer.pubsub.push.exception.handler.JnPubSubPushExceptionHandler;

@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {
		PubSubConsumerController.class,
		JnPubSubPushExceptionHandler.class
})
@SpringBootApplication
public class PubSubPushApplicationStarter {

	
	public static void main(String[] args) {
		CcpDependencyInjection.loadAllDependencies
		(
				new Http(),
				new JsonHandler(),
				new InstantMessenger(),
				new TextExtractor(),
				new FileBucket(),
				new DbUtils(),
				new Email(),
				new Query(),
				new Bulk(),
				new Dao()
		);

		SpringApplication.run(PubSubPushApplicationStarter.class, args);
	}

	
}
