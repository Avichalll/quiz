package com.quiz.quiz.Model.File;

import com.quiz.quiz.common.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Files extends BaseEntity {

    private String fileName;
    private String filePath;
}
