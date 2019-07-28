package com.batch.process;

import org.springframework.batch.item.ItemProcessor;

import com.batch.entity.Industry;

public class IndustryProcessor implements ItemProcessor<Industry, Industry>{
    @Override
    public Industry process(final Industry industry) {
        final String year = industry.getIndutryYear();
        final String industryCode = industry.getIndutryCode();
        final String industryName = industry.getIndutryName();
        final String rmeVariable = industry.getRmeVariable();
        final String rmeSize = industry.getRmeSize();
        final String rmeUnit = industry.getRmeUnit();
        final String rmeValue = industry.getRmeValue();
        
        final Industry processedIndustry = new Industry();
        processedIndustry.setIndutryYear(year);
        processedIndustry.setIndutryCode(industryCode);
        processedIndustry.setIndutryName(industryName);
        processedIndustry.setRmeVariable(rmeVariable);
        processedIndustry.setRmeSize(rmeSize);
        processedIndustry.setRmeUnit(rmeUnit);
        processedIndustry.setRmeValue(rmeValue);
        return processedIndustry;
    }
}