package com.example.springbatch;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FlatFileItemWriterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private int chunkSize = 5;
    private final DataSource dataSource;
    
    @Bean
    public Job job() {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
        		.start(step1())
                .build();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(chunkSize)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public FlatFileItemWriter<Object> customItemWriter() {
        return new FlatFileItemWriterBuilder<>()
        		.name("flatFileWriter")
        		.resource(new FileSystemResource("C:\\Users\\samsung\\git\\repository\\basic_config\\src\\main\\resources\\customer.txt"))
        		.append(true)// 기존 데이터에 추가
        		.shouldDeleteIfEmpty(true) // 데이터가 없으면 파일 삭제
        		.delimited()
        		.delimiter("|")
        		.names(new String[] {"id", "name", "age"})
        		.build();
        		
	}

    @Bean
	public ItemReader<Customer> customItemReader() {
    	List<Customer> customer = Arrays.asList(new Customer(1L, "moon", 41),
    			new Customer(2L, "moon2", 42),
    			new Customer(3L, "moon3", 43));
    	
//    	ListItemReader<Customer> reader = new ListItemReader<>(customer);
    	ListItemReader<Customer> reader = new ListItemReader<>(Collections.emptyList()); // 객체 빈값
    	return reader;
				
	
	}

}