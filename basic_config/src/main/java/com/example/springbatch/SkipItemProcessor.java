package com.example.springbatch;

import org.springframework.batch.item.ItemProcessor;

public class SkipItemProcessor implements ItemProcessor<String, String> {

	private int cnt;
	@Override
	public String process(String item) throws Exception {
		
		if(item.equals("6") || item.equals("7")) {
			System.out.println(" >> ItemProcess : "+ item);
			throw new SkippableException("Process failed : " + cnt  );
		}else {
			System.out.println(" >> ItemProcess : "+ item);
			
			return String.valueOf(Integer.valueOf(item) * -1);
		}
	}
}
