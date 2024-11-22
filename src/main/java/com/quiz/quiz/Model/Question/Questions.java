package com.quiz.quiz.Model.Question;

import com.quiz.quiz.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder

public class Questions extends BaseEntity {

    @NotBlank(message = "Question Should Not Blank")
    private String question;

    @NotBlank(message = "Answer1 Should Not Blank")
    private String answer1;

    @NotBlank(message = "Answer2 Should Not Blank")
    private String answer2;
    @NotBlank(message = "Answer3 Should Not Blank")

    private String answer3;
    @NotBlank(message = "Answer4 Should Not Blank")

    private String answer4;
    @NotBlank(message = "Correct Answer Should Not Blank")
    private String correctAnswer;

    private String category;
    private String level;

}
