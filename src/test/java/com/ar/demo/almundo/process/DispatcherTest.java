package com.ar.demo.almundo.process;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ar.demo.almundo.domain.Call;
import com.ar.demo.almundo.domain.Employee;
import com.ar.demo.almundo.domain.StatusCall;
import com.ar.demo.almundo.domain.StatusEmployee;
import com.ar.demo.almundo.domain.TypeEmployee;
import com.ar.demo.almundo.domain.process.Dispatcher;
import com.ar.demo.almundo.domain.process.Task;
import com.ar.demo.almundo.services.CallService;
import com.ar.demo.almundo.services.EmployeesService;

public class DispatcherTest {

	private Dispatcher dispatcher;
	private CallService callService;
	private EmployeesService employeesService;
	
	@Before
	public void setUp() throws Exception {
		dispatcher = new Dispatcher();
		callService = new CallService();
		employeesService = new EmployeesService();
		BasicConfigurator.configure();
	}

	@Test
	public void testGetTaks() {
		dispatcher.setEmployees(employeesService.getEmployeesByType(4, 2, 1));
		List<Call> calls = callService.getCalls(5);
		List<Task> tasks=dispatcher.getTaks(calls);
		Assert.assertEquals(tasks.size(), 5);
		Assert.assertEquals(dispatcher.getCallsQueue().size(), 0);
		
		dispatcher.setEmployees(employeesService.getEmployeesByType(20, 12, 5));
		calls = callService.getCalls(50);
		tasks=dispatcher.getTaks(calls);
		Assert.assertEquals(tasks.size(), 37);
		Assert.assertEquals(dispatcher.getCallsQueue().size(),13);
	}
	@Test
	public void testGetEmployeeFree() {
		// 2 Operators
		//1 Supervisor
		// 1 director
		dispatcher.setEmployees(employeesService.getEmployeesByType(2, 1, 1));
		
		Employee emp =dispatcher.getEmployeeFree().get();
		Assert.assertEquals(emp.getType(), TypeEmployee.OPERATOR);
		emp.setStatus(StatusEmployee.BUSY);
		
		emp =dispatcher.getEmployeeFree().get();
		Assert.assertEquals(emp.getType(), TypeEmployee.OPERATOR);
		emp.setStatus(StatusEmployee.BUSY);
		
		emp =dispatcher.getEmployeeFree().get();
		Assert.assertEquals(emp.getType(), TypeEmployee.SUPERVISOR);
		emp.setStatus(StatusEmployee.BUSY);
		
		emp =dispatcher.getEmployeeFree().get();
		Assert.assertEquals(emp.getType(), TypeEmployee.DIRECTOR);
		emp.setStatus(StatusEmployee.BUSY);
		
		Assert.assertFalse(dispatcher.getEmployeeFree().isPresent());
		
	}
	
	@Test
	public void testDispatch5CallsAnd10Employees() {
		List<Call> calls = callService.getCalls(5);
		Map<TypeEmployee, List<Employee>> employees = employeesService.getEmployeesByType(5, 3, 2);
		dispatcher.setEmployees(employees);
		dispatcher.dispatchCalls(calls);
		
		//After We have processed the calls have changed their status to DONE and Employees are free
		Assert.assertEquals(calls.stream().filter(call -> call.getStatus().equals(StatusCall.DONE)).count(),5L);
		
		Assert.assertEquals(employees.values().stream()
				 														  .flatMap(map ->  map.stream())
				 														  .collect(Collectors.toList())
				 														  .stream()
				 														  .filter(emp -> emp.getStatus()
				 														  .equals(StatusEmployee.FREE))
				 														  .count(), 10L);
	}
	
	@Test
	public void testDispatch15CallsAnd16Employees() {
		List<Call> calls = callService.getCalls(15);
		Map<TypeEmployee, List<Employee>> employees =employeesService.getEmployeesByType(10, 4, 2);
		dispatcher.setEmployees(employees);
		dispatcher.dispatchCalls(calls);
		
		//After We have processed the calls have changed their status to DONE and Employees are free
		Assert.assertEquals(calls.stream().filter(call -> call.getStatus().equals(StatusCall.DONE)).count(),15L);
		
		Assert.assertEquals(employees.values().stream()
				  .flatMap(map ->  map.stream())
				  .collect(Collectors.toList())
				  .stream()
				  .filter(emp -> emp.getStatus()
				  .equals(StatusEmployee.FREE))
				  .count(), 16L);
	}
	@Test
	public void testDispatch25CallsAnd10Employees() {
		List<Call> calls = callService.getCalls(25);
		Map<TypeEmployee, List<Employee>> employees = employeesService.getEmployeesByType(15, 8, 6);
		dispatcher.setEmployees(employees);
		dispatcher.dispatchCalls(calls);
		
		//After We have processed the calls have changed their status to DONE and Employees are free
		Assert.assertEquals(calls.stream().filter(call -> call.getStatus().equals(StatusCall.DONE)).count(),25L);
				
		Assert.assertEquals(employees.values().stream()
				  .flatMap(map ->  map.stream())
				  .collect(Collectors.toList())
				  .stream()
				  .filter(emp -> emp.getStatus()
				  .equals(StatusEmployee.FREE))
				  .count(), 29L);
	}
	@Test
	public void testDispatch10CallsAnd10Employees() {
		List<Call> calls = callService.getCalls(10);
		Map<TypeEmployee, List<Employee>> employees = employeesService.getEmployeesByType(6, 3, 1);
		dispatcher.setEmployees(employees);
		dispatcher.dispatchCalls(calls);
		
		//After We have processed the calls have changed their status to DONE and Employees are free
		Assert.assertEquals(calls.stream().filter(call -> call.getStatus().equals(StatusCall.DONE)).count(),10L);
		
		Assert.assertEquals(employees.values().stream()
				  .flatMap(map ->  map.stream())
				  .collect(Collectors.toList())
				  .stream()
				  .filter(emp -> emp.getStatus()
				  .equals(StatusEmployee.FREE))
				  .count(), 10L);
	}
	@Test
	public void testDispatch6CallsAnd3Employees() {
		
		List<Call> calls = callService.getCalls(6);
		Map<TypeEmployee, List<Employee>> employees = employeesService.getEmployeesByType(1, 1, 1);
		dispatcher.setEmployees(employees);
		dispatcher.dispatchCalls(calls);
		
		//After We have processed the calls have changed their status to DONE and Employees are free
		Assert.assertEquals(calls.stream().filter(call -> call.getStatus().equals(StatusCall.DONE)).count(),6L);
		
		Assert.assertEquals(employees.values().stream()
				  .flatMap(map ->  map.stream())
				  .collect(Collectors.toList())
				  .stream()
				  .filter(emp -> emp.getStatus().equals(StatusEmployee.FREE))
				  .count(), 3L);
	}
	@Test
	public void testDispatch15CallsAnd10Employees() {
		
		List<Call> calls = callService.getCalls(15);
		Map<TypeEmployee, List<Employee>> employees = employeesService.getEmployeesByType(6, 3, 1);
		dispatcher.setEmployees(employees);
		dispatcher.dispatchCalls(calls);
		
		//After We have processed the calls have changed their status to DONE and Employees are free
		Assert.assertEquals(calls.stream().filter(call -> call.getStatus().equals(StatusCall.DONE)).count(),15L);
		
		Assert.assertEquals(employees.values().stream()
				  .flatMap(map ->  map.stream())
				  .collect(Collectors.toList())
				  .stream()
				  .filter(emp -> emp.getStatus().equals(StatusEmployee.FREE))
				  .count(), 10L);
	}
	@Test
	public void testExistsFreeEmployee() {
		Map<TypeEmployee, List<Employee>> employees = employeesService.getEmployeesByType(1, 1, 1);
		dispatcher.setEmployees(employees);
		Assert.assertTrue(dispatcher.existsFreeEmployee());
		employees.values().stream()
										.flatMap(map ->  map.stream())
										.collect(Collectors.toList())
										.forEach(emp->{emp.setStatus(StatusEmployee.BUSY);});
		Assert.assertFalse(dispatcher.existsFreeEmployee());
	}
}
