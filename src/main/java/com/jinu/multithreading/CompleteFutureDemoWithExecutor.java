package com.jinu.multithreading;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.jinu.multithreading.dto.Employee;

public class CompleteFutureDemoWithExecutor {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Executor executor = Executors.newFixedThreadPool(5);
		CompletableFuture<?> future = CompletableFuture.runAsync(() -> System.out.println("runAsync demo"),executor);
		CompletableFuture<?> future2 = CompletableFuture.runAsync(() -> {
			System.out.println(Thread.currentThread().getName());
			System.out.println("runAsync demo");
		},executor);

		CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
			System.out.println(Thread.currentThread().getName());
			System.out.println("supplyAsync demo");
			return "sample return";
		},executor);
		System.out.println(future3.get());

		CompletableFuture<List<Employee>> empList = CompletableFuture
				.supplyAsync(CompleteFutureDemoWithExecutor::createEmployeeList,executor);
		System.out.println("Printing employee  list 1");
		empList.get().forEach(System.out::println);

		CompletableFuture<List<Employee>> empList2 = CompletableFuture
				.supplyAsync(CompleteFutureDemoWithExecutor::createEmployeeListWithDelay,executor);
		System.out.println("Printing employee list 2");
		empList2.get().forEach(System.out::println);
		empList2.join();
		System.out.println("thenApplyAsync and thenAcceptAsync  demo ");
		CompletableFuture<Void> empList3 = CompletableFuture
				.supplyAsync(CompleteFutureDemoWithExecutor::createEmployeeList,executor).thenApplyAsync((employees) -> {
					System.out.println("Inside thenApplyAsync " + Thread.currentThread().getName());
					System.out.println("Employees size recieved : " + employees.size());
					List<Employee> empListFiltered = employees.stream().filter(employee -> employee.getAge() > 30)
							.collect(Collectors.toList());
					System.out.println("stream completed " + empListFiltered.size());
					return empListFiltered;
				},executor).thenAcceptAsync((employees) -> {
					System.out.println("Inside thenAcceptAsync " + Thread.currentThread().getName());
					System.out.println("employees size recieved : " + employees.size());
					employees.stream().forEach(e -> System.out.println(e.toString()));
				},executor);
		empList3.join();
		System.out.println("thenApplyAsync and thenAcceptAsync  demo finished");
	}

	private static List<Employee> createEmployeeList() {
		return IntStream.rangeClosed(1, 10).boxed().map(i -> new Employee(i)).collect(Collectors.toList());

	}

	private static List<Employee> createEmployeeListWithDelay() {
		System.out.println("Thread  " + Thread.currentThread().getName());
		return IntStream.rangeClosed(1, 10).boxed().peek(e -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}).map(i -> new Employee(i)).collect(Collectors.toList());

	}
}
