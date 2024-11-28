package com.quiz.quiz.Model.QuestionController;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class QuestionResponse {

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
    private String level;

}
