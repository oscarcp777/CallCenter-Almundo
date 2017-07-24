package com.ar.demo.almundo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.ar.demo.almundo.domain.Employee;
import com.ar.demo.almundo.domain.TypeEmployee;

/**
 * Mock que simula un servicio o un consumer que traeria los empleados
 * @author oscar
 *
 */
public class EmployeesService {

	/**
	 *     Metodo que simula la llamada a un servicios que provee los empleados por tipo de empleados 
	 *     
	 *     recibo como parametros  la cantidad  de operadores, supervisores y directores
	 *     
	 * @param operators  Cantidad pedida de operadores
	 * @param supervisors Cantidad pedida de supervisores
	 * @param directors  Cantidad pedida de directores
	 * @return   Devuelve en un map  las listas de de operadores, supervisores y directores
	 */
	public Map<TypeEmployee, List<Employee>> getEmployeesByType(Integer operators, Integer supervisors, Integer directors) {
		
		Map<TypeEmployee, List<Employee>> employees = new HashMap<TypeEmployee, List<Employee>>();
		employees.put(TypeEmployee.OPERATOR, new ArrayList<Employee>());
		employees.put(TypeEmployee.SUPERVISOR, new ArrayList<Employee>());
		employees.put(TypeEmployee.DIRECTOR, new ArrayList<Employee>());

		IntStream.range(1, operators+1).forEach((val)->{
			employees.get(TypeEmployee.OPERATOR).add(new Employee(TypeEmployee.OPERATOR,val));
		});
		IntStream.range( operators+1,operators +supervisors+1).forEach((val)->{
			employees.get(TypeEmployee.SUPERVISOR).add(new Employee(TypeEmployee.SUPERVISOR,val));
		});
		IntStream.range(operators +supervisors+1, operators +supervisors+directors+1).forEach((val)->{
			employees.get(TypeEmployee.DIRECTOR).add(new Employee(TypeEmployee.DIRECTOR,val));
		});
		
		return employees;
	}


}
