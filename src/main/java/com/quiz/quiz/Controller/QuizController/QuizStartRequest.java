package com.quiz.quiz.Controller.QuizController;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuizStartRequest {

    @NotNull(message = "Name should not  Null")
    @NotBlank(message = "Name Should not Blank ")
    private String name;

    @NotNull(message = "Email Should not null")
    @NotBlank(message = "Email Should not Blank")
    private String email;

}
