package com.ar.demo.almundo.services;


import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ar.demo.almundo.domain.Call;
import com.ar.demo.almundo.domain.StatusCall;

public class CallServicesTest {
	private CallService service;
	@Before
	public void setUp() throws Exception {

		service = new CallService();
	}
	
	@Test
	public void testGetCalls() {

		List<Call> calls= service.getCalls(2);
		Assert.assertEquals(calls.size(), 2);

		calls= service.getCalls(12);
		Assert.assertEquals(calls.size(), 12);

		List<Integer> durations = calls.stream().map(call -> call.getDuration()).collect(Collectors.toList());
		durations.forEach(duration -> {
			Assert.assertTrue( CallService.TIME_DURATION_MIN >= duration || duration <= CallService.TIME_DURATION_MAX );
		});
		System.out.println(calls.stream().filter(call -> call.getStatus().equals(StatusCall.WAITING)).count());
	}

}
