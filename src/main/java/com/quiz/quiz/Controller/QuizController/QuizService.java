package com.quiz.quiz.Controller.QuizController;

import org.springframework.stereotype.Service;

import com.quiz.quiz.Model.Question.QuestionRepo;
import com.quiz.quiz.Model.Quiz.QuizAttempt;
import com.quiz.quiz.Model.Quiz.QuizAttemptRepository;
import com.quiz.quiz.User.Userrepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class QuizService {

    private final Userrepository userrepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionRepo questionRepo;

    private final QuizMapper quizMapper;

    public String startQuiz(QuizStartRequest quizStartRequest) {

        QuizAttempt quizAttempt = quizMapper.toQuizAttemp(quizStartRequest);
        quizAttemptRepository.save(quizAttempt);

        return "started";

    }

}
