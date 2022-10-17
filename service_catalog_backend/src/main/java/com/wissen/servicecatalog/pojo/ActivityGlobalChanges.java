package com.wissen.servicecatalog.pojo;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityGlobalChanges {
    private Integer activityId;

    @NotBlank(message="Please enter the activity")
    @NotNull(message = "Please enter the activity ")
    private String activityName;

    @NotBlank(message="Please enter the category")
    @NotNull(message = "Please enter the category")
    private String category;


    @NotBlank(message="Please enter the facilitator")
    @NotNull(message = "Please enter the facilitator")
    private String facilitator;

    @NotBlank(message="Please enter the service")
    @NotNull(message = "Please enter the service")
    private String service;

    @NotBlank(message="Please enter the technologies")
    @NotNull(message = "Please enter the technologies")
    private String technologies;

    @NotNull(message="Please enter the tower id")
    private List<Integer> towerId;

    @NotBlank(message="Please enter skill")
    @NotNull(message="Please enter skill")
    private String skillLevel;
    
	
    @NotNull(message = "Please enter the minimum Score ")
    private  Integer minimumScore;
	
    @NotNull(message = "Please enter the maximum Score ")
    private Integer maximumScore;
	
    @NotNull(message = "Please enter the next Level Minimum score ")
    private Integer nextLevelMin;

}
