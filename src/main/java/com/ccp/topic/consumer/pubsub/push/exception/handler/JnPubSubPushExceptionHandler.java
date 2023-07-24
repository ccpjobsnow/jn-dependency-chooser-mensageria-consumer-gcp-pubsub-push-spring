package com.ccp.topic.consumer.pubsub.push.exception.handler;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.exceptions.commons.CcpFlow;
import com.ccp.jn.async.business.NotifyError;

@RestControllerAdvice
public class JnPubSubPushExceptionHandler {

	private NotifyError notifyError = CcpDependencyInjection.getInjected(NotifyError.class);

	@ExceptionHandler({ CcpFlow.class })
	@ResponseBody
	public Map<String, Object> handle(CcpFlow e, HttpServletResponse res){
		res.setStatus(e.status);
		return e.values.content;
	}
	
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ Throwable.class })
	public void handle(Throwable e) {
		this.notifyError.sendErrorToSupport(e);
	}
}
