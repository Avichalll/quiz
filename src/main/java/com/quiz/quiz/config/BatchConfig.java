package com.quiz.quiz.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.quiz.quiz.Model.Question.QuestionRepo;
import com.quiz.quiz.Model.Question.Questions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@AllArgsConstructor
@Slf4j
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    @Autowired
    private final QuestionRepo questionRepo;

    @Bean
    @StepScope
    public FlatFileItemReader<Questions> itemReader(@Value("#{jobParameters['fullPathFileName']}") String dynamicPath) {
        FlatFileItemReader<Questions> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource(dynamicPath));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setStrict(false);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    public QuestionProcessor processor() {
        return new QuestionProcessor();
    }

    private LineMapper<Questions> lineMapper() {
        DefaultLineMapper<Questions> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);

        lineTokenizer.setNames("question", "answer1", "answer2", "answer3", "answer4", "correctAnswer", "category",
                "difficultyLevel");
        BeanWrapperFieldSetMapper<Questions> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Questions.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public RepositoryItemWriter<Questions> writer() {
        RepositoryItemWriter<Questions> writer = new RepositoryItemWriter<>();
        writer.setRepository(questionRepo);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step importStep() {
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(5); // Retry up to 5 times
        retryTemplate.setRetryPolicy(retryPolicy);

        return new StepBuilder("csvImport", jobRepository)
                .<Questions, Questions>chunk(10, platformTransactionManager)
                .reader(itemReader(null))
                .processor(processor())
                .writer(writer())
                .faultTolerant()
                .skip(Exception.class)
                .retryLimit(10) // Retry limit for the step
                .retry(CannotAcquireLockException.class)
                // .skipLimit(10) // Add this to skip up to 10 items if necessary
                // .listener(skipListener())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importStudents", jobRepository)
                .start(importStep())
                .build();
    }

    // ! till now i send data in synchornous mode;

    // @Bean
    // public TaskExecutor taskExecutor() {
    // return new SyncTaskExecutor(); // Synchronous execution
    // }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(500);
        return asyncTaskExecutor;
    }

}
