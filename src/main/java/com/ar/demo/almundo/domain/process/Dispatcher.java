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
 * <h3></h3>
 * @author oscar
 *
 */
public class Dispatcher {

	private static final Logger logger = Logger.getLogger(Dispatcher.class);

	private Map<TypeEmployee, List<Employee>> employees = new HashMap<TypeEmployee, List<Employee>>();

	static final Integer  THREAD_NUMBER = 10;

	ExecutorService service = Executors.newFixedThreadPool(THREAD_NUMBER);

	private ConcurrentLinkedQueue<Call> callsQueue = new ConcurrentLinkedQueue<>();

	List<Future<Integer>> futures = new ArrayList<>();

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
	private void processFuture(Future<Integer> future){
		try {
			Integer result = future.get();
			logger.info("Call has been processed  Id : "+result+ "  It's done  :  " + future.isDone());
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Error in threads ", e);
		}
	}
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

	public Boolean existsFreeEmployee() {
		return this.employees.values().stream()
				.flatMap(map ->  map.stream())
				.anyMatch(emp -> ((Employee) emp).getStatus().equals(StatusEmployee.FREE));
	}
	void shutdownAndAwaitTermination() throws InterruptedException {
		service.shutdown(); // Disable new tasks from being submitted
		service.awaitTermination(20L, TimeUnit.SECONDS);
	}

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
