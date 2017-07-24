package com.ar.demo.almundo.domain.process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.ar.demo.almundo.domain.Call;
import com.ar.demo.almundo.domain.Employee;
import com.ar.demo.almundo.domain.StatusEmployee;
import com.ar.demo.almundo.domain.TypeEmployee;
import com.ar.demo.almundo.services.CallService;
/**
 * Clase principal que  procesa las llamadas  decidiendo a que empleado asignr las mismas,
 *         Creando los hilos para procesar 10 llamadas concurrentemente y encolando las llamadas que no pudieron ser procesadas 
 *         para procesar  unos segundos despues
 * @author oscar
 *
 */
public class Dispatcher {

	private static final Logger logger = Logger.getLogger(Dispatcher.class);

	/**
	 * Empleados  agrupados por tipo
	 */
	private Map<TypeEmployee, List<Employee>> employees = new HashMap<TypeEmployee, List<Employee>>();

	static final Integer  THREAD_NUMBER = 10;

	/**
	 * Esta clase es un service que permite definir la cantidad de Threads concurrentes va a procesar
	 * en caso que los Threads sean mayores que las tareas   internamente mantiene una Queue en espera 
	 * hasta poder procesar todas las tareas 
	 */
	ExecutorService service = Executors.newFixedThreadPool(THREAD_NUMBER);

	/**
	 * Queue de llamadas en espera de un empleado disponible
	 */
	private ConcurrentLinkedQueue<Call> callsQueue = new ConcurrentLinkedQueue<>();

	/**
	 * lista con los resultados que devuelven las tareas ejecutadas, los mismos permiten tener un
	 * control de cuando y como resultó la ejecucion de la tarea
	 */
	List<Future<Integer>> futures = new ArrayList<>();
	
	
	
	/** 
	 *     Metodo principal encargado de procesar las llamadas para ello:
	 *   <p> Primero ejecuta concurrentemente como maximo 10 tareas que procesaran las llamadas. 
	 *   Si son mas de 10 llamadas y hay suficientes empleados, el <b>ExecutorService</b> internamente encolara y procesara las tareas sobrantes. 
	 *   Si no hay empleados disponibles se encola la llamada y espera un tiempo minimo para reintentar procesar la llamada
	 *   por ultimo se loguea las resultados y se destruyen los Threrads creados</p>
	 * @param calls
	 */
	public void dispatchCalls(List<Call> calls) {

		try {
			this.futures = service.invokeAll(getTaks(calls));
			processQueue() ;
			this.futures.forEach(future -> processFuture(future));
			shutdownAndAwaitTermination();
		} catch (InterruptedException e) {
			logger.error("Error in dispatchCalls ", e);
		}

	}

	/**
	 * Metodo que wrapea las llamadas en Tareas que ejecutara el  ExecutorService para los empleados 
	 *          disponibles,  si estan todos ocupados encola la llamada  
	 * @param calls
	 * @return
	 */
	public List<Task> getTaks(List<Call> calls){
		List<Task> tasks= new ArrayList<>();

		calls.forEach(call ->{
			Optional<Employee> emp = getEmployeeFree();
			if(emp.isPresent()){
				tasks.add(new Task(call,emp.get()));
			}else{
				callsQueue.add(call);
			}
		});
		return tasks;
	}
	/**
	 *  Loguea los resultados de de la ejecucion de los Threads
	 * @param future
	 */
	private void processFuture(Future<Integer> future){
		try {
			Integer result = future.get();
			logger.info("Call has been processed  Id : "+result+ "  It's done  :  " + future.isDone());
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Error in threads ", e);
		}
	}
	/**
	 *   Metodo que procesa todas las llamadas que quedaron encoladas por falta de empleados disponibles 
	 *           Para ello espera un tiempo minimo y vuelve a preguntar si hay algun empleado disponible
	 * @throws InterruptedException
	 */
	private void processQueue() throws InterruptedException{

		TimeUnit.SECONDS.sleep(CallService.TIME_DURATION_MIN);
		
		while(!this.callsQueue.isEmpty() ){

			if(existsFreeEmployee()){
				Optional<Employee> emp = getEmployeeFree();
				Task task = new Task(this.callsQueue.poll(),emp.get());
				Future<Integer> future =this.service.submit(task);
				futures.add(future);
			}else {
				TimeUnit.SECONDS.sleep(CallService.TIME_DURATION_MIN);
			}
		}



	}

	/**
	 * Verifica en la lista de empleados si hay alguno disponible
	 * @return true cuando hay alguno disponible
	 */
	public Boolean existsFreeEmployee() {
		return this.employees.values().stream()
				.flatMap(map ->  map.stream())
				.anyMatch(emp -> ((Employee) emp).getStatus().equals(StatusEmployee.FREE));
	}
	/**
	 * Apago los Threads  ejecutas i si hay alguno todavia corriendo se espera un determiando tiempo
	 * @throws InterruptedException
	 */
	private void shutdownAndAwaitTermination() throws InterruptedException {
		service.shutdown(); // Disable new tasks from being submitted
		service.awaitTermination(20L, TimeUnit.SECONDS);
	}

	/**
	 * Devuelve un Empleado disponible para ellos, agrupa todos los empleados y los ordena por typo
	 * en el order 
	 *  1.Operador
	 *  2.Supervisor
	 *  3.Director
	 *  y devuelve el primero de esa lista sino hay empleados disponibles devuelve un   Optional vacio
	 * @return
	 */
	public Optional<Employee> getEmployeeFree(){

		Comparator<Employee> empNameComparator = (emp1,emp2)->emp1.getType().getValue() - emp2.getType().getValue();		
		return  this.employees.values().stream()
				.flatMap(map ->  map.stream())
				.collect(Collectors.toList()).stream()
				.filter(emp -> emp.getStatus().equals(StatusEmployee.FREE))
				.sorted(empNameComparator)
				.findFirst();
	}


	public void setEmployees(Map<TypeEmployee, List<Employee>> employees) {
		this.employees= employees;
	}

	public ConcurrentLinkedQueue<Call> getCallsQueue() {
		return callsQueue;
	}

}
