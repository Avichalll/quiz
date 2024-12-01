package com.quiz.quiz.Controller.QuestionController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class QuestionRequest {

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

    // @NotBlank(message = "category Should Not Be Blank")
    private String category;

    // @NotBlank(message = "Level Should Not Be Blank")
    private String level;

}
