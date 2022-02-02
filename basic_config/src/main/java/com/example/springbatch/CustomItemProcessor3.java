package com.example.springbatch;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor3 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {

	@Override
	public ProcessorInfo process(ProcessorInfo item) throws Exception {
		
		System.out.println("CustomItemProcessor 3 ");
		return item;
	}

}
