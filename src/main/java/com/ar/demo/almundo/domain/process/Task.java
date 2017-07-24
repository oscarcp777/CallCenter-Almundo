/**
 * 
 */
package com.ar.demo.almundo.domain.process;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ar.demo.almundo.domain.Call;
import com.ar.demo.almundo.domain.Employee;
import com.ar.demo.almundo.domain.StatusCall;
import com.ar.demo.almundo.domain.StatusEmployee;

/**
 * @author oscar
 *
 */
public class Task implements Callable<Integer> {

	private static final Logger logger = Logger.getLogger(Task.class);

	private Call call;

	private Employee employee;

	public Task(Call call, Employee employee) {
		super();
		this.call = call;
		this.employee = employee;
		this.employee.setStatus(StatusEmployee.BUSY);
		this.call.setStatus(StatusCall.ON_CALL);
	}



	@Override
	public Integer call() throws Exception {
		logger.info("  Employee id : " +employee.getId()+ "  type :  "+employee.getType()+"  is processing call " + call.getId());

		TimeUnit.SECONDS.sleep(call.getDuration());

		this.employee.setStatus(StatusEmployee.FREE);
		this.call.setStatus(StatusCall.DONE);
		return call.getId();

	}

}
