package com.ar.demo.almundo.services;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ar.demo.almundo.domain.Employee;
import com.ar.demo.almundo.domain.TypeEmployee;

public class EmployeeServicesTest {
	private EmployeesService service;
	@Before
	public void setUp() throws Exception {
		
		service = new EmployeesService();
	}

	@Test
	public void testGetEmployeesByType() {
	
		Map<TypeEmployee, List<Employee>> employees= service.getEmployeesByType(2, 2, 2);
		Assert.assertEquals(employees.get(TypeEmployee.OPERATOR).size(), 2);
		Assert.assertEquals(employees.get(TypeEmployee.SUPERVISOR).size(), 2);
		Assert.assertEquals(employees.get(TypeEmployee.DIRECTOR).size(), 2);
		
           List<Employee> emps=employees.values().stream().flatMap(map ->  map.stream()).collect(Collectors.toList());
		 
		 List<Integer>  ids=emps.stream().map(v -> v.getId()).collect(Collectors.toList());
		 ids.sort((o1,o2)->o1.compareTo(o2));
		 Assert.assertEquals(ids.toString(), "[1, 2, 3, 4, 5, 6]");
		
		employees= service.getEmployeesByType(20, 5, 2);
		Assert.assertEquals(employees.get(TypeEmployee.OPERATOR).size(), 20);
		Assert.assertEquals(employees.get(TypeEmployee.SUPERVISOR).size(), 5);
		Assert.assertEquals(employees.get(TypeEmployee.DIRECTOR).size(), 2);
		
		emps=employees.values().stream().flatMap(map ->  map.stream()).collect(Collectors.toList());
		 ids=emps.stream().map(v -> v.getId()).collect(Collectors.toList());
		 ids.sort((o1,o2)->o1.compareTo(o2));
		 Assert.assertEquals(ids.toString(), "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27]");
		 		
	}

}
