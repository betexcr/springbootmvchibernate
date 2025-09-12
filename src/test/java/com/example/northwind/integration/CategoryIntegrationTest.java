package com.example.northwind.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryIntegrationTest extends BaseIntegrationTest {

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
    void testGetAllCategories_ShouldReturnCategories() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/categories"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        assertTrue(jsonNode.get("content").size() > 0);
    }

    @Test
    void testGetCategoryById_ShouldReturnCategory() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/categories/1"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals(1, jsonNode.get("id").asInt());
        assertTrue(jsonNode.has("categoryName"));
        assertTrue(jsonNode.has("description"));
    }

    @Test
    void testGetCategoryById_NonExistent_ShouldReturn404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/categories/99999"), String.class);
        
        assertResponseStatus(response, 404);
    }

    @Test
    void testCreateCategory_AsAdmin_ShouldSucceed() throws Exception {
        String categoryJson = """
            {
                "categoryName": "Test Category",
                "description": "A test category for integration testing",
                "picture": null
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(categoryJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/categories"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("id"));
        assertEquals("Test Category", jsonNode.get("categoryName").asText());
        assertEquals("A test category for integration testing", jsonNode.get("description").asText());
    }

    @Test
    void testCreateCategory_AsStaff_ShouldSucceed() throws Exception {
        String categoryJson = """
            {
                "categoryName": "Staff Category",
                "description": "A category created by staff",
                "picture": null
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(categoryJson, createAuthHeaders(staffToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/categories"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 201);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals("Staff Category", jsonNode.get("categoryName").asText());
    }

    @Test
    void testCreateCategory_WithoutAuth_ShouldReturn401() {
        String categoryJson = """
            {
                "categoryName": "Unauthorized Category",
                "description": "This should fail",
                "picture": null
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(categoryJson);
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/categories"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 401);
    }

    @Test
    void testUpdateCategory_AsAdmin_ShouldSucceed() throws Exception {
        // First create a category
        String createJson = """
            {
                "categoryName": "Original Category",
                "description": "Original description",
                "picture": null
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/categories"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdCategory = objectMapper.readTree(createResponse.getBody());
        int categoryId = createdCategory.get("id").asInt();

        // Update the category
        String updateJson = """
            {
                "categoryName": "Updated Category",
                "description": "Updated description",
                "picture": null
            }
            """;

        HttpEntity<String> updateEntity = new HttpEntity<>(updateJson, createAuthHeaders(adminToken));
        ResponseEntity<String> updateResponse = restTemplate.exchange(
            getApiUrl("/categories/" + categoryId), HttpMethod.PUT, updateEntity, String.class);
        
        assertResponseStatus(updateResponse, 200);
        
        JsonNode updatedCategory = objectMapper.readTree(updateResponse.getBody());
        assertEquals("Updated Category", updatedCategory.get("categoryName").asText());
        assertEquals("Updated description", updatedCategory.get("description").asText());
    }

    @Test
    void testDeleteCategory_AsAdmin_ShouldSucceed() throws Exception {
        // First create a category
        String createJson = """
            {
                "categoryName": "To Delete Category",
                "description": "This will be deleted",
                "picture": null
            }
            """;

        HttpEntity<String> createEntity = new HttpEntity<>(createJson, createAuthHeaders(adminToken));
        ResponseEntity<String> createResponse = restTemplate.exchange(
            getApiUrl("/categories"), HttpMethod.POST, createEntity, String.class);
        
        JsonNode createdCategory = objectMapper.readTree(createResponse.getBody());
        int categoryId = createdCategory.get("id").asInt();

        // Delete the category
        HttpEntity<String> deleteEntity = new HttpEntity<>(createAuthHeaders(adminToken));
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
            getApiUrl("/categories/" + categoryId), HttpMethod.DELETE, deleteEntity, String.class);
        
        assertResponseStatus(deleteResponse, 204);

        // Verify category is deleted
        ResponseEntity<String> getResponse = restTemplate.getForEntity(
            getApiUrl("/categories/" + categoryId), String.class);
        assertResponseStatus(getResponse, 404);
    }

    @Test
    void testGetCategoriesWithPagination_ShouldReturnPaginatedResults() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/categories?page=0&size=3"), String.class);
        
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
    void testGetCategoriesWithSorting_ShouldReturnSortedResults() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getApiUrl("/categories?sort=categoryName,desc"), String.class);
        
        assertResponseStatus(response, 200);
        
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.has("content"));
        assertTrue(jsonNode.get("content").isArray());
        
        // Verify sorting (should be in descending order by categoryName)
        JsonNode content = jsonNode.get("content");
        if (content.size() > 1) {
            String firstName = content.get(0).get("categoryName").asText();
            String secondName = content.get(1).get("categoryName").asText();
            assertTrue(firstName.compareTo(secondName) >= 0, 
                "Categories should be sorted in descending order by name");
        }
    }

    @Test
    void testCreateCategoryWithInvalidData_ShouldReturn400() {
        String invalidCategoryJson = """
            {
                "categoryName": "",
                "description": "Empty name should fail",
                "picture": null
            }
            """;

        HttpEntity<String> entity = new HttpEntity<>(invalidCategoryJson, createAuthHeaders(adminToken));
        ResponseEntity<String> response = restTemplate.exchange(
            getApiUrl("/categories"), HttpMethod.POST, entity, String.class);
        
        assertResponseStatus(response, 400);
    }
}

