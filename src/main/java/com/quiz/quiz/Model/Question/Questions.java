package com.quiz.quiz.Model.Question;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
// @SuperBuilder

public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @NotBlank(message = "Question Should Not Blank")
    private String question;

    // @NotBlank(message = "Answer1 Should Not Blank")
    private String answer1;

    // @NotBlank(message = "Answer2 Should Not Blank")
    private String answer2;
    // @NotBlank(message = "Answer3 Should Not Blank")

    private String answer3;
    // @NotBlank(message = "Answer4 Should Not Blank")

    private String answer4;
    // @NotBlank(message = "Correct Answer Should Not Blank")
    private String correctAnswer;

    private String category;
    private String difficultyLevel;

}
