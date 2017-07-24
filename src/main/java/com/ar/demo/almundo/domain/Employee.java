package com.ar.demo.almundo.domain;
/**
 * Clase que modela un Empleado que tiene un tipo,
 *         que puede cambiar de status y un ID
 *         El empleado por Defecto tiene un status Libre
 * @author oscar
 *
 */
public class Employee {

	private TypeEmployee type;
	private Integer id;
	private StatusEmployee status;
	
	public Employee(TypeEmployee type, Integer id) {
		super();
		this.type = type;
		this.id = id;
		this.status = StatusEmployee.FREE;
	}
	
	public TypeEmployee getType() {
		return type;
	}

	public Integer getId() {
		return id;
	}

	public StatusEmployee getStatus() {
		return status;
	}

	public void setStatus(StatusEmployee status) {
		this.status = status;
	}
}
