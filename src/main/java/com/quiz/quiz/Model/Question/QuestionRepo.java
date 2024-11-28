package com.quiz.quiz.Model.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuestionRepo extends JpaRepository<Questions, Integer>, JpaSpecificationExecutor<Questions> {

}
