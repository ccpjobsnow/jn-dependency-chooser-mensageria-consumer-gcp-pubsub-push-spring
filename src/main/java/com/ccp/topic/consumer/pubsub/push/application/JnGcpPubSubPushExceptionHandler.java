package com.ccp.topic.consumer.pubsub.push.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.jn.async.business.JnAsyncBusinessNotifyError;

@RestControllerAdvice
public class JnGcpPubSubPushExceptionHandler {


	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ Throwable.class })
	public void handle(Throwable e) {
		CcpJsonRepresentation values = new CcpJsonRepresentation(e);
		CcpJsonRepresentation renameKey = values.renameKey("message", "msg");
		JnAsyncBusinessNotifyError jnAsyncBusinessNotifyError = new JnAsyncBusinessNotifyError();
		jnAsyncBusinessNotifyError.apply(renameKey);
	}
}
