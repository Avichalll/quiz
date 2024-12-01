package com.quiz.quiz.Controller.QuestionController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.quiz.quiz.Model.Question.QuestionRepo;
import com.quiz.quiz.Model.Question.Questions;
import com.quiz.quiz.common.PageResponse;

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
        QuestionResponse questionResponse = questionMapper.toQuestionResponse(question);
        return questionResponse;

    }

    public PageResponse<QuestionResponse> getQuizQuestion(String category, String difficultyLevel, int page, int size) {

        Specification<Questions> spec = Specification.where(null);
        if (category != null) {
            spec = spec.and(QuestionSpecification.withCategory(category));
        }

        if (difficultyLevel != null) {
            spec = spec.and(QuestionSpecification.withDifficultyLevel(difficultyLevel));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Questions> questions = questionRepo.findAll(spec, pageable);

        List<QuestionResponse> questionResponse = questions.stream().map(questionMapper::toQuestionResponse)
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

    public String deleteAllQuestion() {
        questionRepo.deleteAll();
        return "deleted";
    }

}
