package com.wissen.servicecatalog.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreUpdateRequest {

    @NotNull(message="Please enter the score id")
    Integer scoreId;
    @Size(min = 0,max = 3,message = "Minimum score is 0 & max is 3")
    Integer score;
    @NotNull(message="Please enter the feedback")
    String feedback;
    @NotBlank(message="please enter roadmap")
    String roadMap;
    @NotNull(message="please enter emplloyee status")
    String employeeStatus;
    @NotNull(message="please enter skill id")
    Integer projectSkillId;

}