package com.example.springbatch;

import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ChunkOrientedTaskletConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
    public Job batchJob() {
        return jobBuilderFactory.get("batchJob")
            .start(step1())
            .next(step2())
            .build();
    }

 
    @Bean
    @JobScope
    public Step step1() {
    	
        return stepBuilderFactory.get("step1")
            .<String, String>chunk(2)
            .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5", "item6")))
            .processor(new ItemProcessor<String, String>() {

				@Override
				public String process(String item) throws Exception {
					return "my_" + item;
				}
            	
			})
            .writer(items ->{
            	Thread.sleep(1000);
            	items.forEach(item ->{
            		System.out.println("items = "+ item);
            	}
            			) ;
            	System.out.println("============== ");
			})
            .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
    	    .tasklet((contribution, chunkContext) -> {
    	    	System.out.println("step2 has executed");
            	return RepeatStatus.FINISHED;
            })
            .build();
    }
}
