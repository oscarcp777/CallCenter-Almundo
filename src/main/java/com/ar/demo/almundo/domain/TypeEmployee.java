package com.ar.demo.almundo.domain;

/**
 * Modela los tipos que pueden ser los empleados
 * @author oscar
 *
 */
public enum TypeEmployee {

	DIRECTOR(3),OPERATOR(1),SUPERVISOR(2);
	
	private final Integer value;

	 private TypeEmployee(Integer value) {
	   this.value = value;
	 }

	 public int getValue() {
	   return value;
	 }
}
