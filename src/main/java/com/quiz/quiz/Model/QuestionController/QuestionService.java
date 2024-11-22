package com.quiz.quiz.Model.QuestionController;

import org.springframework.stereotype.Service;

import com.quiz.quiz.Model.Question.QuestionRepo;
import com.quiz.quiz.Model.Question.Questions;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepo questionRepo;
    private final QuestionMapper questionMapper;

    public String savedQuestion(QuestionRequest questionRequest) {

        Questions questions = questionMapper.toQuestion(questionRequest);
        questionRepo.save(questions);
        return "Question saved Successfully";
    }

    public QuestionResponse getQuestion(Integer questionId) {

        Questions question = questionRepo.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question Not Availabe with Id : " + questionId));
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getQuestion'");
    }

}
