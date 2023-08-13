package com.ccp.jn;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.decorators.CcpTimeDecorator;
import com.ccp.dependency.injection.CcpDependencyInject;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.http.CcpHttpRequester;
import com.ccp.especifications.http.CcpHttpResponse;
import com.ccp.implementations.db.dao.elasticsearch.Dao;
import com.ccp.implementations.db.utils.elasticsearch.DbUtils;
import com.ccp.implementations.http.apache.mime.Http;
import com.jn.commons.JnEntity;
import com.jn.commons.JnTopic;

public class LoginTesting {

	private static final String BASE_URL = "http://localhost:8080/";
	private Helper helper;
	
	public LoginTesting() {
		CcpDependencyInjection.loadAllImplementationsProviders(
				new Http()
				,new DbUtils()
				,new Http()
				,new Dao()

				);
		this.helper = CcpDependencyInjection.getInjected(Helper.class);
		JnEntity.loadEntitiesMetadata();
	}


	@Test
	public void testing() {
		
		this.ourUserIsAccessingJobsNowByTheFirstTime();
		String asyncTaskId = this.soOurUserRequestsToJobsNowToSendsLoginToken();
		this.soOurUserWaitsJobsNowSendLoginTokenByEmail(asyncTaskId);
		
	}


	private void soOurUserWaitsJobsNowSendLoginTokenByEmail(String asyncTaskId) {
		
		for(int k = 0; k < 10; k++) {
			CcpHttpResponse r1 = this.helper.ccpHttp.executeHttpRequest(BASE_URL
					+ "async/task/" + asyncTaskId, "GET", new CcpMapDecorator(), new CcpMapDecorator().asJson());
			
			boolean mensageriaHasBeenRequested = r1.httpStatus == 200;
			assertTrue(mensageriaHasBeenRequested);
			CcpMapDecorator json = r1.asSingleJson();
			new CcpTimeDecorator().sleep(1000);
			
			boolean stillIsRunning = json.containsKey("finished") == false;
			
			if(stillIsRunning) {
				continue;
			}
			
			boolean success = json.getAsBoolean("success");
			assertTrue(success);
			
			boolean mailMessageHasBeenSent = JnEntity.email_message_sent.exists(new CcpMapDecorator()
					.put("subjectType" ,JnTopic.sendUserToken.name())
					.put("email", "teste@ccpjobsnow.com")
					);
			
			assertTrue(mailMessageHasBeenSent);
			
			return;
		}
		assertTrue(false);
	}


	private String soOurUserRequestsToJobsNowToSendsLoginToken() {
		CcpHttpResponse r1 = this.helper.ccpHttp.executeHttpRequest(BASE_URL
				+ "/login/teste@ccpjobsnow.com/token/language/portuguese", "POST", new CcpMapDecorator(), new CcpMapDecorator().asJson());
		
		String asyncTaskId = new CcpMapDecorator(r1.httpResponse).getAsString("asyncTaskId");
		boolean theUserTokenHasBeenRequestedInTheJobsNow = r1.httpStatus == 200;
		assertTrue(theUserTokenHasBeenRequestedInTheJobsNow);

		assertTrue(asyncTaskId.trim().isEmpty() == false);
		return asyncTaskId;
	}


	private void ourUserIsAccessingJobsNowByTheFirstTime() {
		CcpHttpResponse r1 = this.helper.ccpHttp.executeHttpRequest(BASE_URL
				+ "login/teste@ccpjobsnow.com/token", "HEAD", new CcpMapDecorator(), new CcpMapDecorator().asJson());
	
		boolean userHasNotBeenCreated = r1.httpStatus == 404;
		assertTrue(userHasNotBeenCreated);
	}
	public static class Helper {
		@CcpDependencyInject
		private CcpHttpRequester ccpHttp;
		
	}
}

