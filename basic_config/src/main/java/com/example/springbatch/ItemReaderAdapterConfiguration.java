package com.example.springbatch;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ItemReaderAdapterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private int chunkSize = 10;
    
    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
        		.start(step1())
                .build();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(chunkSize)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemWriter<String> customItemWriter() {
        return items -> {
        	System.out.println(items);
        };
	}

    @Bean
	public ItemReader<String> customItemReader() throws Exception {
    	
    	ItemReaderAdapter<String> reader = new ItemReaderAdapter<>();
    	reader.setTargetObject(customService());
    	reader.setTargetMethod("customRead");
    	
    	return reader;
				
	
	}

    @Bean
	public CustomService customService() {
		 return new CustomService();
	}

}