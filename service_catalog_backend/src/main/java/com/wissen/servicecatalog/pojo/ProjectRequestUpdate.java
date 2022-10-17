package com.wissen.servicecatalog.pojo;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequestUpdate {
	@NotNull(message = "Please enter the project name")
	private String projectName;
	@NotNull(message = "Please enter the project manager id")
	private Integer projectManagerId;
	@NotNull(message="Please enter the manager name ")
	private String projectManagerName;
	@NotEmpty(message="Please Select the tower's for the respective project")
	private List<Integer> towerId;

}