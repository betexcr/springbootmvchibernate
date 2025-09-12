package com.example.northwind.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeIntegrationTest extends BaseIntegrationTest {

    private String adminToken;
    private String staffToken;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        adminToken = loginAndGetToken("admin", "x");
        staffToken = loginAndGetToken("staff", "x");
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllEmployees_AsStaff_ShouldReturnEmployees() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/employees"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        assertTrue(jsonNode.get("content").size() > 0);
    }

    @Test
    void testGetAllEmployees_AsAdmin_ShouldReturnEmployees() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/employees"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        assertTrue(jsonNode.get("content").size() > 0);
    }

    @Test
    void testGetAllEmployees_WithoutAuth_ShouldReturn401() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/employees"), String.class);
        
        assertResponseStatus(response, 401);
    }

    @Test
    void testGetEmployeeById_AsStaff_ShouldReturnEmployee() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/employees/1"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals(1, jsonNode.get("id").asInt());
        assertTrue(jsonNode.has("firstName"));
        assertTrue(jsonNode.has("lastName"));
        assertTrue(jsonNode.has("title"));
    }

    @Test
    void testGetEmployeeById_NonExistent_ShouldReturn404() {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/employees/99999"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 404);
    }

    @Test
    void testCreateEmployee_AsAdmin_ShouldSucceed() throws Exception {
        String employeeJson = """
            {
                "firstName": "John",
                "lastName": "Doe",
                "title": "Sales Representative",
                "titleOfCourtesy": "Mr.",
                "birthDate": "1980-01-01",
                "hireDate": "2020-01-01",
                "address": "123 Test Street",
                "city": "Test City",
                "region": "Test Region",
                "postalCode": "12345",
                "country": "Test Country",
                "homePhone": "555-0123",
                "extension": "1234",
                "notes": "Test employee",
                "reportsTo": null,
                "photoPath": null
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(employeeJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/employees"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("id"));
        assertEquals("John", jsonNode.get("firstName").asText());
        assertEquals("Doe", jsonNode.get("lastName").asText());
        assertEquals("Sales Representative", jsonNode.get("title").asText());
        assertEquals("Test City", jsonNode.get("city").asText());
    }

    @Test
    void testCreateEmployee_AsStaff_ShouldSucceed() throws Exception {
        String employeeJson = """
            {
                "firstName": "Jane",
                "lastName": "Smith",
                "title": "Account Manager",
                "titleOfCourtesy": "Ms.",
                "birthDate": "1985-05-15",
                "hireDate": "2021-03-01",
                "address": "456 Staff Avenue",
                "city": "Staff City",
                "region": "Staff Region",
                "postalCode": "54321",
                "country": "Staff Country",
                "homePhone": "555-0567",
                "extension": "5678",
                "notes": "Staff employee",
                "reportsTo": null,
                "photoPath": null
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(employeeJson, createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/employees"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals("Jane", jsonNode.get("firstName").asText());
        assertEquals("Smith", jsonNode.get("lastName").asText());
        assertEquals("Account Manager", jsonNode.get("title").asText());
    }

    @Test
    void testCreateEmployee_WithoutAuth_ShouldReturn401() {
        String employeeJson = """
            {
                "firstName": "Unauthorized",
                "lastName": "User",
                "title": "Manager",
                "titleOfCourtesy": "Mr.",
                "birthDate": "1990-01-01",
                "hireDate": "2022-01-01",
                "address": "789 Unauthorized St",
                "city": "Unauthorized City",
                "region": "Unauthorized Region",
                "postalCode": "99999",
                "country": "Unauthorized Country",
                "homePhone": "555-0999",
                "extension": "9999",
                "notes": "Unauthorized employee",
                "reportsTo": null,
                "photoPath": null
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(employeeJson);
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/employees"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 401);
    }

    @Test
    void testUpdateEmployee_AsAdmin_ShouldSucceed() throws Exception {
        // First create an employee
        String createJson = """
            {
                "firstName": "Original",
                "lastName": "Employee",
                "title": "Original Title",
                "titleOfCourtesy": "Mr.",
                "birthDate": "1980-01-01",
                "hireDate": "2020-01-01",
                "address": "Original Address",
                "city": "Original City",
                "region": "Original Region",
                "postalCode": "00000",
                "country": "Original Country",
                "homePhone": "555-0000",
                "extension": "0000",
                "notes": "Original employee",
                "reportsTo": null,
                "photoPath": null
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/employees"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdEmployee = objectMapper.readTree(createResponse.getBody());
        int employeeId = createdEmployee.get("id").asInt();

        // Update the employee
        String updateJson = """
            {
                "firstName": "Updated",
                "lastName": "Employee",
                "title": "Updated Title",
                "titleOfCourtesy": "Dr.",
                "birthDate": "1980-01-01",
                "hireDate": "2020-01-01",
                "address": "Updated Address",
                "city": "Updated City",
                "region": "Updated Region",
                "postalCode": "11111",
                "country": "Updated Country",
                "homePhone": "555-1111",
                "extension": "1111",
                "notes": "Updated employee",
                "reportsTo": null,
                "photoPath": null
            }
            """;

        HttpEntity<String> updateEntity = new HttpEntity<>(updateJson, createAuthHeaders(adminToken));
        ResponseEntity<String> updateResponse = restTemplate.exchange(
            getApiUrl("/employees/" + employeeId), HttpMethod.PUT, updateEntity, String.class);
        
        assertResponseStatus(updateResponse, 200);
        
        JsonNode updatedEmployee = objectMapper.readTree(updateResponse.getBody());
        assertEquals("Updated", updatedEmployee.get("firstName").asText());
        assertEquals("Updated Title", updatedEmployee.get("title").asText());
        assertEquals("Updated City", updatedEmployee.get("city").asText());
    }

    @Test
    void testDeleteEmployee_AsAdmin_ShouldSucceed() throws Exception {
        // First create an employee
        String createJson = """
            {
                "firstName": "To Delete",
                "lastName": "Employee",
                "title": "Delete Title",
                "titleOfCourtesy": "Mr.",
                "birthDate": "1980-01-01",
                "hireDate": "2020-01-01",
                "address": "Delete Address",
                "city": "Delete City",
                "region": "Delete Region",
                "postalCode": "22222",
                "country": "Delete Country",
                "homePhone": "555-2222",
                "extension": "2222",
                "notes": "Delete employee",
                "reportsTo": null,
                "photoPath": null
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/employees"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdEmployee = objectMapper.readTree(createResponse.getBody());
        int employeeId = createdEmployee.get("id").asInt();

        // Delete the employee
        HttpEntity<String> deleteEntity = new HttpEntity<>(createAuthHeaders(adminToken));
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
            getApiUrl("/employees/" + employeeId), HttpMethod.DELETE, deleteEntity, String.class);
        
        assertResponseStatus(deleteResponse, 204);

        // Verify employee is deleted
        HttpEntity<String> getEntity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> getResponse = restTemplate.exchange(
            getApiUrl("/employees/" + employeeId), HttpMethod.GET, getEntity, String.class);
        assertResponseStatus(getResponse, 404);
    }

    @Test
    void testGetEmployeesWithPagination_ShouldReturnPaginatedResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/employees?page=0&size=3"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.has("totalElements"));
        assertTrue(jsonNode.has("totalPages"));
        assertTrue(jsonNode.has("size"));
        assertTrue(jsonNode.has("number"));
        
        assertEquals(3, jsonNode.get("size").asInt());
        assertEquals(0, jsonNode.get("number").asInt());
        assertTrue(jsonNode.get("content").size() <= 3);
    }

    @Test
    void testGetEmployeesWithSorting_ShouldReturnSortedResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/employees?sort=lastName,asc"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // Verify sorting (should be in ascending order by lastName)
        JsonNode content = jsonNode.get("content");
        if (content.size() > 1) {
            String firstName = content.get(0).get("lastName").asText();
            String secondName = content.get(1).get("lastName").asText();
            assertTrue(firstName.compareTo(secondName) <= 0, 
                "Employees should be sorted in ascending order by last name");
        }
    }

    @Test
    void testCreateEmployeeWithInvalidData_ShouldReturn400() {
        String invalidEmployeeJson = """
            {
                "firstName": "",
                "lastName": "Valid Last Name",
                "title": "Valid Title",
                "titleOfCourtesy": "Mr.",
                "birthDate": "1980-01-01",
                "hireDate": "2020-01-01",
                "address": "Valid Address",
                "city": "Valid City",
                "region": "Valid Region",
                "postalCode": "33333",
                "country": "Valid Country",
                "homePhone": "555-3333",
                "extension": "3333",
                "notes": "Valid employee",
                "reportsTo": null,
                "photoPath": null
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(invalidEmployeeJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/employees"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 400);
    }

    @Test
    void testSearchEmployees_ShouldReturnFilteredResults() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/employees?q=andrew"), HttpMethod.GET, entity, String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // Should find employees containing "andrew"
        boolean foundAndrew = false;
        for (JsonNode employee : jsonNode.get("content")) {
            String firstName = employee.get("firstName").asText().toLowerCase();
            String lastName = employee.get("lastName").asText().toLowerCase();
            if (firstName.contains("andrew") || lastName.contains("andrew")) {
                foundAndrew = true;
                break;
            }
        }
        assertTrue(foundAndrew, "Should find employees containing 'andrew'");
    }
}

