package com.example.springbatch;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<ProcessorInfo, ProcessorInfo> {

	@Override
	public ProcessorInfo process(ProcessorInfo item) throws Exception {
		
		System.out.println("CustomItemProcessor 1 ");
		return item;
	}

}
