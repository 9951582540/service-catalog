package com.wissen.servicecatalog.pojo;

import java.util.List;

import com.wissen.servicecatalog.entity.Tower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

	
	private Integer projectId;
	private String  projectName;
	private String  projectManagerName;
	private Integer projectManagerId;
	private List<Tower> tower;

}
