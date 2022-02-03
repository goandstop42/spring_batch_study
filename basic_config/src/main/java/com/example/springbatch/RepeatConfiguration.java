package com.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionTimedOutException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RepeatConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int chunkSize = 5;
    
    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
        		.start(step1())
                .build();
    }

	@Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<String,String>chunk(chunkSize)
                .reader(new ItemReader<String>() {
                	int i = 0;

					@Override
					public String read()
							throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
						i++;
						
						return i > 3 ? null : "item" + i;
					}
                	
				})
                .processor(new ItemProcessor<String, String>() {

                	RepeatTemplate repeatTemplate = new RepeatTemplate();
					@Override
					public String process(String item) throws Exception {
						 
//						repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(3));
//						repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(3000));
						
//						CompositeCompletionPolicy completionPolicy = new CompositeCompletionPolicy();
//						CompletionPolicy[] completionPolicies = new CompletionPolicy[]{
//																	new SimpleCompletionPolicy(3)
//																	,new TimeoutTerminationPolicy(3000)};
						
					 	repeatTemplate.setExceptionHandler(simpleLimitExceptionHandler());
					 	
						repeatTemplate.iterate(new RepeatCallback() {
							
							@Override
							public RepeatStatus doInIteration(RepeatContext context) throws Exception {
								
								System.out.println("repeatTempate is testing");
								throw new RuntimeException("Exception is occurred");
								
//								return RepeatStatus.CONTINUABLE;
							}
						});
						
						return item;
					}
				})
                .writer(items ->System.out.println(items))
                .build();
    }
    
    @Bean
	public ExceptionHandler simpleLimitExceptionHandler() {
		return new SimpleLimitExceptionHandler(3);
	}

}