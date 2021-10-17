package com.jinu.multithreading.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Data;

@Data
public class Employee {
	private static List<Integer> ageList = IntStream.rangeClosed(25, 50).boxed().collect(Collectors.toList());
	private int empId;
	private String empName;
	private int age;
	
	public Employee(int i) {
		empId = i;
		empName = "Employee"+i;
		Collections.shuffle(ageList);
		age = ageList.get(0);
	}
	
}
