package com.quiz.quiz.Model.QuestionController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/Question")
@AllArgsConstructor

public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/save")
    public ResponseEntity<?> saveQuestion(@Valid @RequestBody QuestionRequest questionRequest) {
        return ResponseEntity.ok(questionService.savedQuestion(questionRequest));
    }

    @GetMapping("getQuestion/{questionId}")
    public ResponseEntity<?> getQuestion(@PathVariable("questionId") Integer questionId) {
        return ResponseEntity.ok(questionService.getQuestion(questionId));
    }

}
