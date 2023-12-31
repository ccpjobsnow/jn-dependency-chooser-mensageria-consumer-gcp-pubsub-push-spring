package com.ccp.topic.consumer.pubsub.push.controller;

import java.util.Base64;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.jn.async.JnAsyncBusiness;

@CrossOrigin
@RestController
@RequestMapping(value = "/{topic}")
public class JnGcpPubSubConsumerController {

	@PostMapping
	public void onReceiveMessage(@PathVariable("topic") String topic, @RequestBody Map<String, Object> body) {
		CcpJsonRepresentation ccpMapDecorator = new CcpJsonRepresentation(body);
		CcpJsonRepresentation internalMap = ccpMapDecorator.getInnerJson("message");
		String data = internalMap.getAsString("data");
		byte[] decode = Base64.getDecoder().decode(data);
		String str = new String(decode);
		CcpJsonRepresentation json = new CcpJsonRepresentation(str);
		JnAsyncBusiness.executeProcess(topic, json);
	}

	@PostMapping("/testing")
	public void onReceiveMessageTesting(@PathVariable("topic") String topic, @RequestBody Map<String, Object> json) {
		CcpJsonRepresentation md = new CcpJsonRepresentation(json);
		JnAsyncBusiness.execute(topic, md);
	}
	
}
