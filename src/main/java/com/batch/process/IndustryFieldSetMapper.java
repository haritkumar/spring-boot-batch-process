package com.batch.process;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

import com.batch.entity.Industry;

@Component
public class IndustryFieldSetMapper implements FieldSetMapper<Industry> {
	    @Override
	    public Industry mapFieldSet(FieldSet fieldSet) {
	        final Industry industry = new Industry();
	        industry.setIndutryYear(fieldSet.readString("year"));
	        industry.setIndutryCode(fieldSet.readString("industry_code_ANZSIC"));
	        industry.setIndutryName(fieldSet.readString("industry_name_ANZSIC"));
	        industry.setRmeSize(fieldSet.readString("rme_size_grp"));
	        industry.setRmeVariable(fieldSet.readString("variable"));
	        industry.setRmeValue(fieldSet.readString("value"));
	        industry.setRmeUnit(fieldSet.readString("unit"));
	        
	        return industry;
	    }
	}
