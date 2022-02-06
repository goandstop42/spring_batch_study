package com.example.springbatch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class SkipItemWriter implements ItemWriter<String> {

	private int cnt;

	@Override
	public void write(List<? extends String> items) throws Exception {
		
		for(String item : items) {
			
			if(item.equals("-12")){
				throw new SkippableException("Process failed : " + cnt  );
			}else {
				System.out.println("ItemWriter : "+ item);
				
			}
		}
	}
}
