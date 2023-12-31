package com.ccp.topic.consumer.pubsub.push.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ccp.jn.async.business.JnAsyncBusinessNotifyError;

@RestControllerAdvice
public class JnGcpPubSubPushExceptionHandler {

	private JnAsyncBusinessNotifyError notifyError = new JnAsyncBusinessNotifyError();

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ Throwable.class })
	public void handle(Throwable e) {
		this.notifyError.apply(e);
	}
}
