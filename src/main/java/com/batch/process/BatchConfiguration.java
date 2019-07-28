package com.batch.process;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.batch.entity.Industry;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public FlatFileItemReader<Industry> reader() {
		return new FlatFileItemReaderBuilder<Industry>()
				.name("induatryItemReader")
				.resource(new ClassPathResource("batch.csv"))
				.delimited()
				.names(new String[] { "year", "industry_code_ANZSIC", "industry_name_ANZSIC", "rme_size_grp", "variable", "value", "unit" })
				.lineMapper(lineMapper())
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Industry>() {{
						setTargetType(Industry.class);
					}})
				.build();
	}
	
	@Bean
    public LineMapper<Industry> lineMapper() {
        final DefaultLineMapper<Industry> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_COMMA);
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] { "year", "industry_code_ANZSIC", "industry_name_ANZSIC", "rme_size_grp", "variable", "value", "unit" });
        final IndustryFieldSetMapper fieldSetMapper = new IndustryFieldSetMapper();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }
	
	@Bean
    public IndustryProcessor processor() {
        return new IndustryProcessor();
    }
	
	@Bean
    public JdbcBatchItemWriter<Industry> writer(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Industry>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO industry (indutry_year, indutry_code, indutry_name, rme_size, rme_unit, rme_value, rme_variable, status) VALUES (:indutryYear, :indutryCode, :indutryName, :rmeSize, :rmeUnit, :rmeValue, :rmeVariable, 'UNPROCESSED')")
                .dataSource(dataSource)
                .build();
    }
	
	@Bean
    public Step step1(JdbcBatchItemWriter<Industry> writer) {
        return stepBuilderFactory.get("step1")
                .<Industry, Industry> chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
	
	
	@Bean
    public Job importVoltageJob(NotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importIndustryJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
}
