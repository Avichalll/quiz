package com.quiz.quiz.Model.QuestionController;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quiz.quiz.File.FileStorageService;
import com.quiz.quiz.File.FilesDetails;
import com.quiz.quiz.File.FilesRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/Question")
@AllArgsConstructor
@Slf4j

public class QuestionController {

    private final JobLauncher jobLauncher;
    private final Job job;

    private final QuestionService questionService;

    private final FileStorageService fileStorageService;

    private final FilesRepository filesRepository;

    @PostMapping("/save")
    public ResponseEntity<?> saveQuestion(@Valid @RequestBody QuestionRequest questionRequest) {
        return ResponseEntity.ok(questionService.savedQuestion(questionRequest));
    }

    @GetMapping("getQuestion/{questionId}")
    public ResponseEntity<?> getQuestion(@PathVariable("questionId") Integer questionId) {
        return ResponseEntity.ok(questionService.getQuestion(questionId));
    }

    // @PostMapping(value = "/upload/{fileName}", consumes = "multipart/form-data")
    // public String importCsvToDb(@RequestBody String entity) {
    // // TODO: process POST request

    // return entity;
    // }
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String importCsvToDBJob(@RequestParam("file") MultipartFile file) {

        // fileStorageService.saveFile(file, "Student");

        FilesDetails filesDetails = new FilesDetails();

        filesDetails.setFilePath(fileStorageService.saveFile(file, "Student").replace("\\", "/"));

        filesRepository.save(filesDetails);
        String filepathofcsv = filesDetails.getFilePath();
        log.info("file path of file of csv" + filepathofcsv);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fullPathFileName", filepathofcsv)
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();
        log.info("file passd to Jobparameter successfully");

        try {
            jobLauncher.run(job, jobParameters);
            log.info("job Launch Successfully");
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {
            e.printStackTrace();

        }

        return "saved";
    }

}
