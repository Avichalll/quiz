package com.quiz.quiz.Controller.QuizController;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.quiz.quiz.Model.Quiz.QuizAttempt;

@Service
public class QuizMapper {

    public QuizAttempt toQuizAttemp(QuizStartRequest quizStartRequest) {
        return QuizAttempt.builder()
                .name(quizStartRequest.getName())
                .email(quizStartRequest.getEmail())
                .startedAt(LocalDateTime.now())
                .build();
    }

}
