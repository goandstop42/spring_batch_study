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
public class ChunkConfig {
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
            .<String, String>chunk(5)
            .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5")))
            .processor(new ItemProcessor<String, String>() {

				@Override
				public String process(String item) throws Exception {
					Thread.sleep(300);
					System.out.println("item = " + item);
					return "my" + item;
				}
            	
			})
            .writer(items ->{
            	Thread.sleep(300);
            	System.out.println("items = "+ items);
			})
            .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
    	    .tasklet((contribution, chunkContext) -> {
            	return RepeatStatus.FINISHED;
            })
            .build();
    }
}
