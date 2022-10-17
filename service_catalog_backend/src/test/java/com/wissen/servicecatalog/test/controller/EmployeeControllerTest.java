package com.wissen.servicecatalog.test.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.pojo.EmployeUpdateRequest;
import com.wissen.servicecatalog.pojo.EmployeeRequest;
import com.wissen.servicecatalog.pojo.EmployeeResponse;
import com.wissen.servicecatalog.pojo.ManagerResponse;
import com.wissen.servicecatalog.pojo.ResourceDetails;
import com.wissen.servicecatalog.service.impl.EmployeeServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

	@MockBean
	AuthenticationManager authenticationManager;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	EmployeeServiceImpl employeeService;
	
	@MockBean
	EmployeeMaster employeeMaster;

	EmployeeRequest employeeRequest = new EmployeeRequest(1,5874, "pawan", 1, 6, 5555, "asd", "dsa", "dsad", "dasd",
			"pawan@wisseninfotech.com", "kjcs");

	ResourceDetails ResourceDetails = new ResourceDetails();

	String statusJson = null;

	ApplicationRoleMaster applicationRoleMaster = new ApplicationRoleMaster(1, "employee");

	EmployeUpdateRequest employeUpdateRequstemployee = new EmployeUpdateRequest(8, 6, "java", true, "developer",
			"springboot", "fullstack");

	Employee employee = new Employee(1, "ab1", "pawan@134", "pawan123", LocalDateTime.now(), applicationRoleMaster,
			"active", "pvcsa");

	EmployeeResponse employeeResponse = new EmployeeResponse(1,"pawan", 5874, 8, 6, 1234, "java", "springboot", "active",
			"developer", "abcd", "pawan@wisseninfotech.com", "sgfas");

	ManagerResponse managerResponse = new ManagerResponse("pawan", 5874, 8, 6, 1234, "java", "springboot", "active",
			"developer", "abcd", "pawan@wisseninfotech.com", "sgfas");

	@Test
	public void getAllEmployee() throws Exception {

		ArrayList<EmployeeResponse> list = new ArrayList<>();
		list.add(employeeResponse);
		when(employeeService.getAllEmployee()).thenReturn(list);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/employee/getAll");

		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();
		assertEquals(200, status);
		assertNotNull(list);

	}

	@Test
	void addEmployee() throws Exception {
		
		statusJson = null;
		statusJson = mapper.writeValueAsString(employeeRequest);
		
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/service-catalog/employee/add")
				.contentType(MediaType.APPLICATION_JSON).content(statusJson);

		when(employeeService.addEmployee(employeeRequest)).thenReturn(null);
		
		ResultActions perform = mockMvc.perform(requestBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(200, status);

	}

	@Test
	public void getEmployee() throws Exception {

		when(employeeService.getEmployee(ArgumentMatchers.anyInt())).thenReturn(employeeResponse);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/service-catalog/employee/get/5874");

		ResultActions perform = mockMvc.perform(requestBuilder);

		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();

		assertEquals(200, status);

		assertEquals("pawan", employeeResponse.getEmployeeName());

	}

	@Test
	public void updateEmployee() throws Exception {

		List<EmployeeResponse> employeeResponseList = new LinkedList<>();
		employeeResponseList.add(employeeResponse);
		List<EmployeeRequest> employeeRequest = new LinkedList<>();
		List<EmployeeMaster> employeeRequest1 = new LinkedList<>();
		statusJson = null;
		statusJson = mapper.writeValueAsString(employeeRequest);
		when(employeeService.updateEmployee(employeeRequest)).thenReturn(employeeRequest1);

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/service-catalog/employee/update")
				.contentType(MediaType.APPLICATION_JSON).content(statusJson);

		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		assertEquals(200, status);

	}

	@Test
	public void getAllResourceDetails() throws Exception {

		Set<ResourceDetails> list = new HashSet<>();
		list.add(ResourceDetails);
		when(employeeService.getAllResourceDetails()).thenReturn(list);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/service-catalog/employee/resource-details/getAll");

		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();
		assertEquals(200, status);
		assertNotNull(list);

	}

	@Test
	public void getAllManager() throws Exception {

		List<ManagerResponse> list = new ArrayList<>();
		list.add(managerResponse);
		when(employeeService.getAllManager()).thenReturn(list);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/service-catalog/employee/manager/getAll");

		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();

		int status = response.getStatus();
		assertEquals(200, status);
		assertNotNull(list);
	}

	@Test
	public void updateEmployeeDetails() throws Exception {

		statusJson = null;
		statusJson = mapper.writeValueAsString(employeUpdateRequstemployee);
		when(employeeService.updateByEmployee(employeUpdateRequstemployee, 5874)).thenReturn("employeeResponse");

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.patch("/service-catalog/employee/updateDetails/5874").contentType(MediaType.APPLICATION_JSON)
				.content(statusJson);

		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		System.out.println(status);
		assertEquals(200, status);

		assertEquals("fullstack", employeUpdateRequstemployee.getCertification());

	}

	@Test
	public void requestUpdate() throws Exception {
		statusJson = null;
		statusJson = mapper.writeValueAsString(employeeResponse);
		when(employeeService.requestUpdate(5874, "pawan", "pawan@13.com")).thenReturn("employeeResponse");

		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.patch("/service-catalog/employee/requestUpdate/5874/pawan/pawan@13.com")
				.contentType(MediaType.APPLICATION_JSON).content(statusJson);

		ResultActions perform = mockMvc.perform(requestBuilder);
		MvcResult mvcResult = perform.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		assertEquals(200, status);
		assertEquals(5874, employeeResponse.getEmployeeId());

	}
}
