package com.ar.demo.almundo.domain;

/**
 * Clase que modela una llamada que tiene una duración x,  que puede cambiar de estado y un ID
 * @author oscar
 *
 */
public class Call {
	
	private Integer duration;
	private Integer id;
	private StatusCall status;
	
	public Call(Integer duration, Integer id) {
		super();
		this.duration = duration;
		this.id = id;
		this.status = StatusCall.WAITING;
	}

	public Integer getDuration() {
		return duration;
	}

	public Integer getId() {
		return id;
	}

	public StatusCall getStatus() {
		return status;
	}

	public void setStatus(StatusCall status) {
		this.status = status;
	}

}
