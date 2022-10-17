package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishScore {

    @NotNull(message = "Please enter the score id")
    Integer scoreId;
    @NotNull(message = "Please select the score")
    Integer score;
}
