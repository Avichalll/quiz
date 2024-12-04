package com.quiz.quiz.Controller.QuizController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.quiz.quiz.Controller.QuestionController.QuestionMapper;
import com.quiz.quiz.Controller.QuestionController.QuestionResponse;
import com.quiz.quiz.Model.Question.QuestionRepo;
import com.quiz.quiz.Model.Question.Questions;
import com.quiz.quiz.Model.Quiz.QuizAttempt;
import com.quiz.quiz.Model.Quiz.QuizAttemptRepository;
import com.quiz.quiz.User.Userrepository;
import com.quiz.quiz.common.PageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final Userrepository userrepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionRepo questionRepo;

    private final QuestionMapper questionMapper;

    private final QuizMapper quizMapper;

    public PageResponse<QuestionResponse> startQuiz(QuizStartRequest quizStartRequest, int page, int size) {

        QuizAttempt quizAttempt = quizMapper.toQuizAttemp(quizStartRequest);
        quizAttemptRepository.save(quizAttempt);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Questions> questions = questionRepo.findAll(pageable);

        List<QuestionResponse> questionResponse = questions.stream().limit(15).map(questionMapper::toQuestionResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                questionResponse,
                questions.getNumber(),
                questions.getSize(),
                questions.getTotalElements(),
                questions.getTotalPages(),
                questions.isFirst(),
                questions.isLast());

    }

}
