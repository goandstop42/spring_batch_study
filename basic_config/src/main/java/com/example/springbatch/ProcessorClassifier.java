package com.example.springbatch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

public class ProcessorClassifier<C, T> implements Classifier<C, T> {

	private Map<Integer,  ItemProcessor<ProcessorInfo, ProcessorInfo>> processMap = new HashMap<>();
	@SuppressWarnings("unchecked")
	@Override
	public T classify(C classifiable) {
		return (T)processMap.get(((ProcessorInfo) classifiable).getId());
	}

	public void setProcessMap(Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processMap) {
		this.processMap = processMap;
	}
	

}
