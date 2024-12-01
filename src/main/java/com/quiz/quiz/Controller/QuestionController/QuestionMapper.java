package com.quiz.quiz.Controller.QuestionController;

import org.springframework.stereotype.Service;

import com.quiz.quiz.Model.Question.Questions;

@Service
public class QuestionMapper {

    public Questions toQuestion(QuestionRequest questionRequest) {
        return Questions.builder()
                .question(questionRequest.getQuestion())
                .answer1(questionRequest.getAnswer1())
                .answer2(questionRequest.getAnswer2())
                .answer3(questionRequest.getAnswer3())
                .answer4(questionRequest.getAnswer4())
                .correctAnswer(questionRequest.getCorrectAnswer())
                .category(questionRequest.getCategory())
                .difficultyLevel(questionRequest.getLevel())
                .build();
    }

    public QuestionResponse toQuestionResponse(Questions questions) {
        return QuestionResponse.builder()
                .question(questions.getQuestion())
                .answer1(questions.getAnswer1())
                .answer2(questions.getAnswer2())
                .answer3(questions.getAnswer3())
                .answer4(questions.getAnswer4())
                .correctAnswer(questions.getCorrectAnswer())
                .level(questions.getDifficultyLevel())
                .build();
    }

}
