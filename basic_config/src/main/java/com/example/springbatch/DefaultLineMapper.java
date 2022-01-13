package com.example.springbatch;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;

public class DefaultLineMapper<T> implements LineMapper {

	private LineTokenizer tokenizer;
	private FieldSetMapper<T> fieldSetMapper;
	
	@Override
	public T mapLine(String line, int lineNumber) throws Exception {
		 return fieldSetMapper.mapFieldSet(tokenizer.tokenize(line));
	}

	public void setSetLineTokenizeri(LineTokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	public void setFieldSetMapper(FieldSetMapper<T> fieldSetMapper) {
		
		this.fieldSetMapper = fieldSetMapper;
	}

	
}
