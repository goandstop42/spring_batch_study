package com.example.springbatch;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FlatFilesConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job batchJob() {
		return jobBuilderFactory.get("batchJob")
//        	.incrementer(new RunIdIncrementer())
				.start(step1()).next(step2()).build();
	}

	@SuppressWarnings("unchecked")
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<String, String>chunk(5)
				.reader(itemReader())
				.writer(new ItemWriter() {
					@Override
					public void write(List items) throws Exception {
						System.out.println("itmes = " + items);
				}

		}).build();
	}

	@Bean
	public ItemReader itemReader() {
		FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
		itemReader.setResource(new ClassPathResource("/customer.csv"));

		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
		lineMapper.setSetLineTokenizeri(new DelimitedLineTokenizer());
		lineMapper.setFieldSetMapper(new CustomerFieldSetMapper());

		itemReader.setLineMapper(lineMapper);
		itemReader.setLinesToSkip(1);

		return itemReader;

	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2").tasklet((contribution, chunkContext) -> {
			System.out.println("step2 has executed");
			return RepeatStatus.FINISHED;
		}).build();
	}
}