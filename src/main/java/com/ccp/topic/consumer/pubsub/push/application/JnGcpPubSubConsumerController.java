package com.ccp.topic.consumer.pubsub.push.application;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.exceptions.process.CcpAsyncProcess;
import com.jn.commons.entities.JnEntityAsyncTask;

@CrossOrigin
@RestController
@RequestMapping(value = "/{topic}")
public class JnGcpPubSubConsumerController {

	@PostMapping
	public void onReceiveMessage(@PathVariable("topic") String topic, @RequestBody Map<String, Object> body) {
		CcpJsonRepresentation ccpMapDecorator = new CcpJsonRepresentation(body);
		CcpJsonRepresentation internalMap = ccpMapDecorator.getInnerJson("message");
		String data = internalMap.getAsString("data");
		String str = new CcpStringDecorator(data).text().asBase64();
		CcpJsonRepresentation json = new CcpJsonRepresentation(str);
		JnEntityAsyncTask entity = new JnEntityAsyncTask();
		CcpAsyncProcess.executeProcess(topic, json, entity);
	}

	@PostMapping("/testing")
	public void onReceiveMessageTesting(@PathVariable("topic") String topic, @RequestBody Map<String, Object> json) {
		CcpJsonRepresentation md = new CcpJsonRepresentation(json);
		CcpAsyncProcess.executeProcess(topic, md);
	}
	
}
