package com.ccp.topic.consumer.pubsub.push.controller;

import java.util.Base64;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.async.AsyncServices;
import com.ccp.process.CcpProcess;

@CrossOrigin
@RestController
@RequestMapping(value = "/{topic}")
public class PubSubConsumerController {

	@PostMapping
	public void onReceiveMessage(@PathVariable("topic") String topic, @RequestBody Map<String, Object> body) {
		CcpProcess process = AsyncServices.catalog.getAsObject(topic);
		CcpMapDecorator ccpMapDecorator = new CcpMapDecorator(body);
		CcpMapDecorator internalMap = ccpMapDecorator.getInternalMap("message");
		String data = internalMap.getAsString("data");
		byte[] decode = Base64.getDecoder().decode(data);
		String str = new String(decode);
		CcpMapDecorator json = new CcpMapDecorator(str);
		process.execute(json);
	}
	
}