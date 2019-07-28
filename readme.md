## Spring boot batch process
Process large csv to mysql db
![Batch Processing](asset/batch.png)

### batch.csv 
- 1804 rows 
- 7 coln
- 156 KB size

### Upload csv rest api 
Upload batch.csv using any rest client

![Upload](asset/1.png)
![Upload](asset/2.png)

### DB structure after running batch job
![Upload](asset/3.png)

#### Batch config
```java
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	@StepScope
	public FlatFileItemReader<Industry> reader(@Value("#{jobParameters[csvPath]}") String csvPath) {
		return new FlatFileItemReaderBuilder<Industry>()
				.name("induatryItemReader")
				.resource(new FileSystemResource(csvPath))
				.delimited()
				.names(new String[] { "year", "industry_code_ANZSIC", "industry_name_ANZSIC", "rme_size_grp", "variable", "value", "unit" })
				.lineMapper(lineMapper())
				.linesToSkip(1)//skip header line
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
                .reader(reader(null))
                .processor(processor())
                .writer(writer)
                .build();
    }
	
	
	@Bean("importIndustryJob")
    public Job importIndustryJob(NotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importIndustryJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
}
```

#### Execute job from controller
```java
@Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job importIndustryJob;
    
	@Async
	public void processBatch(String n_name) {
		logger.info("Processing "+n_name);
		try {
			JobParameters jobParameters = new JobParametersBuilder()
					.addLong("time", new Date().getTime())
					.addString("csvPath", n_name)
					.toJobParameters();
			jobLauncher.run(importIndustryJob, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}
```