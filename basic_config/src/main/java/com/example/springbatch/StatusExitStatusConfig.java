package com.example.springbatch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StatusExitStatusConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
    public Job batchJob() {
        return jobBuilderFactory.get("batchJob")
            .incrementer(new RunIdIncrementer())
            .start(flow())
            .next(step3())
            .end()
            .build();
    }

	@Bean
	public Flow flow() {
		FlowBuilder<Flow> builder = new FlowBuilder<>("flow");
		builder.start(step1())
			.next(step2())
			.end();
		return builder.build();
	}
 
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
            .tasklet((contribution, chunkContext) -> {
                log.info("step1 was executed");
//	                throw new RuntimeException("step1 was failed.");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
            .tasklet((contribution, chunkContext) -> {
                log.info("step2 was execute");
//	                throw new RuntimeException("step2 was failed.");
                contribution.setExitStatus(ExitStatus.FAILED);
                return RepeatStatus.FINISHED;
            })
            .build();
    }
    @Bean
    public Step step3() {
    	return stepBuilderFactory.get("step3")
    			.tasklet((contribution, chunkContext) -> {
    				log.info("step3 was execute");
//	                throw new RuntimeException("step2 was failed.");
    				contribution.setExitStatus(ExitStatus.FAILED);
    				return RepeatStatus.FINISHED;
    			})
    			.build();
    }

}
