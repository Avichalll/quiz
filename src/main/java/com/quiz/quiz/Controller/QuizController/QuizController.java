package com.quiz.quiz.Controller.QuizController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/Quiz")
@RequiredArgsConstructor
public class QuizController {

    @Autowired
    private final QuizService quizService;

    @PostMapping("/startQuiz")
    public ResponseEntity<?> startQuiz(@RequestBody QuizStartRequest quizStartRequest) {

        return ResponseEntity.ok(quizService.startQuiz(quizStartRequest));
    }

}
