package com.wissen.servicecatalog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponse {
	private Integer activityId;
	private String activityName;
    private String category;
    private String service;
    private String facilitator;
    private String technologies;
    private String skill;
}
