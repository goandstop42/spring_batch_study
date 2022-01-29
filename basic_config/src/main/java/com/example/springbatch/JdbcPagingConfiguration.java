package com.example.springbatch;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JdbcPagingConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int chunkSize = 2;
    private final DataSource dataSource;
    
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
                .<Customer, Customer>chunk(2)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemWriter<Customer> customItemWriter() {
        return items -> {
        	System.out.println("==paging==");
            for (Customer item : items) {
                System.out.println(item.toString());
            }
        };
	}

    @Bean
	public ItemReader<Customer> customItemReader() throws Exception {
    	
    	Map<String, Object> parameters = new HashMap<>();
    	parameters.put("firstname", "A%");
    	
    	return new JdbcPagingItemReaderBuilder<Customer>()
    			.name("jdbcPagingItemReader")
    			.pageSize(2)
    			.dataSource(dataSource)
    			.rowMapper(new BeanPropertyRowMapper<>(Customer.class))
    			.queryProvider(createQueryProvider())
    			.parameterValues(parameters)
    			.build();
				
	
	}

    @Bean
	public PagingQueryProvider createQueryProvider() throws Exception {
    	

    	
    	SqlPagingQueryProviderFactoryBean queryProvider =
    			new SqlPagingQueryProviderFactoryBean();
    	queryProvider.setDataSource(dataSource);
    	queryProvider.setSelectClause("id, firstname, lastname, birthdate");
    	queryProvider.setFromClause("from customer");
    	queryProvider.setWhereClause("where firstname like :firstname");
    	
    	Map<String, Order> sortKeys = new HashMap<>();
    	sortKeys.put("id", Order.ASCENDING);
    	sortKeys.put("firstname", Order.ASCENDING);
    	
    	queryProvider.setSortKeys(sortKeys);
		 return queryProvider.getObject();
	}

}