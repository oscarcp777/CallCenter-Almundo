package com.ar.demo.almundo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import com.ar.demo.almundo.domain.Call;

public class CallService {
	
	public static final Integer TIME_DURATION_MIN = 5;
	static final Integer TIME_DURATION_MAX = 10;

	public List<Call> getCalls(Integer  quantity){
		 List<Call> calls = new ArrayList<Call>();
		
		 IntStream.range(1, quantity+1).forEach((val)->{
			 
			 Integer randomNum = ThreadLocalRandom.current().nextInt(TIME_DURATION_MIN, TIME_DURATION_MAX + 1);
			 calls.add(new Call(randomNum,val));
			 
			});
		
		return calls;
	}
}
