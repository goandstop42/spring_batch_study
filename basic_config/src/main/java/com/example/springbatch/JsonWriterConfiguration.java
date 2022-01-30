package com.example.springbatch;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JsonWriterConfiguration {

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
        return new JsonFileItemWriterBuilder<Customer>()
        		.name("jsonFileItemWriter")
        		.jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
        		.resource(new FileSystemResource("C:\\Users\\samsung\\git\\repository\\basic_config\\src\\main\\resources\\customer.json"))
        		.build();
	}

    @Bean
    public Marshaller itemMarshaller() {
		
    	Map<String, Class<?>> alias = new HashMap<>();
    	alias.put("customer", Customer.class );
    	alias.put("id", Long.class);
    	alias.put("firstName", String.class );
    	alias.put("lastName", String.class );
    	alias.put("birthDate", String.class);
    	
    	XStreamMarshaller xStreamMarshaller= new XStreamMarshaller();
    	xStreamMarshaller.setAliases(alias);
    	
    	return xStreamMarshaller;
	}

	@Bean
    public JdbcPagingItemReader<Customer> customItemReader() {

        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(10);
        reader.setRowMapper(new CustomerRowMapper());

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, firstName, lastName, birthdate");
        queryProvider.setFromClause("from customer");
        queryProvider.setWhereClause("where firstname like :firstname");

        Map<String, Order> sortKeys = new HashMap<>(1);

        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);
        reader.setQueryProvider(queryProvider);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("firstname", "A%");

        reader.setParameterValues(parameters);

        return reader;
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