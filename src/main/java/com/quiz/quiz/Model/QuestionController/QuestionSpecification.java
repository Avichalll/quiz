package com.quiz.quiz.Model.QuestionController;

import org.springframework.data.jpa.domain.Specification;

import com.quiz.quiz.Model.Question.Questions;

public class QuestionSpecification {

    public static Specification<Questions> withCategory(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Questions> withDifficultyLevel(String difficultyLevel) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("difficultyLevel"), difficultyLevel);
    }

}
