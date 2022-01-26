package com.example.springbatch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JdbcCursorReaderConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int chunkSize = 10;
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
    public ItemWriter<Customer> customItemWriter() {
        return items -> {
            for (Customer item : items) {
                System.out.println(item.toString());
            }
        };
	}

    @Bean
	public ItemReader<Customer> customItemReader() {
    	return new JdbcCursorItemReaderBuilder<Customer>()
    			.name("jdbcCursorItemReader")
    			.fetchSize(chunkSize)
    			.sql("select id, firstName, lastName, birthdate from customer where firstName like ? order by lastName, firstName")
    			.beanRowMapper(Customer.class)
    			.queryArguments("A%")
    			.dataSource(dataSource)
    			.build();
				
	
	}

}