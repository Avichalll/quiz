package com.quiz.quiz.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.quiz.quiz.Model.Question.Questions;

public class QuestionProcessor implements ItemProcessor<Questions, Questions> {

    @Override
    @Nullable
    public Questions process(@NonNull Questions questions) throws Exception {

        System.out.println("Processing student: " + questions);
        return questions;
    }

}
