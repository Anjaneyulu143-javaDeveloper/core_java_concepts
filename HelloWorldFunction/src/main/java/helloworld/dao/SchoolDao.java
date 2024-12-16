package helloworld.dao;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import helloworld.pojo.Student;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class SchoolDao {
    private final DynamoDbClient dynamoDbClient;
    private static final Logger logger = LoggerFactory.getLogger(SchoolDao.class);

    public SchoolDao(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // 1. Create Item
    public String createItem(String tableName, Student student) {
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException("Table name is empty or null");
        }
        try {
            PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(Map.of(
                    "student_id", AttributeValue.builder().s(student.getId()).build(),
                    "std_name", AttributeValue.builder().s(student.getName()).build(),
                    "std_age", AttributeValue.builder().n(String.valueOf(student.getAge())).build(),
                    "std_grade", AttributeValue.builder().s(student.getGrade()).build()
                ))
                .build();

            PutItemResponse response = dynamoDbClient.putItem(putItemRequest);
            logger.info("Item created successfully: {}", response);
            return "Added successfully. HTTP status code: " + response.sdkHttpResponse().statusCode();
        } catch (DynamoDbException e) {
            logger.error("DynamoDBException while creating item: {}", e.getMessage());
            return "Failed to add item. Error: " + e.getMessage();
        }
    }

    // 2. Read Item using Query by student Id based
    public String readItem(String tableName, String studentId) {
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException("Table name is empty or null");
        }

        try {
            Map<String, AttributeValue> expressionAttributeValues = Map.of(
                ":v_student_id", AttributeValue.builder().s(studentId).build()
            );

            QueryRequest queryRequest = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression("student_id = :v_student_id")
                .expressionAttributeValues(expressionAttributeValues)
                .build();

            QueryResponse queryResponse = dynamoDbClient.query(queryRequest);

            if (!queryResponse.items().isEmpty()) {
                StringBuilder result = new StringBuilder("Student data found:\n");
                for (Map<String, AttributeValue> item : queryResponse.items()) {
                    Student student = new Student();
                    student.setId(item.get("student_id").s());
                    student.setName(item.get("std_name").s());
                    student.setAge(Integer.parseInt(item.get("std_age").n()));
                    student.setGrade(item.get("std_grade").s());
                    result.append(student.toString()).append("\n");
                    logger.info("Item Found: {}", student.getName());
                }
                return result.toString();
            } else {
                logger.error("No item found for student_id: {}", studentId);
                return "No student found for student_id: " + studentId;
            }
        } catch (DynamoDbException e) {
            logger.error("Error querying items from DynamoDB: {}", e.getMessage());
            return "Failed to query items. Error: " + e.getMessage();
        }
    }

    // 3. Update Item
    public String updateItem(String tableName, Student student) {
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException("Table name is empty or null");
        }

        try {
            Map<String, AttributeValue> key = Map.of(
                "student_id", AttributeValue.builder().s(student.getId()).build(),
                "std_name", AttributeValue.builder().s(student.getName()).build()
            );

            Map<String, AttributeValueUpdate> updates = Map.of(
                "std_age", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().n(String.valueOf(student.getAge())).build())
                    .action(AttributeAction.PUT)
                    .build(),
                "std_grade", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(student.getGrade()).build())
                    .action(AttributeAction.PUT)
                    .build()
            );

            UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .attributeUpdates(updates)
                .build();

            UpdateItemResponse response = dynamoDbClient.updateItem(updateItemRequest);
            logger.info("Item updated successfully: {}", response.sdkHttpResponse().statusCode());
            return "Updated the item. HTTP status code: " + response.sdkHttpResponse().statusCode();
        } catch (DynamoDbException e) {
            logger.error("Error updating item in DynamoDB: {}", e.getMessage());
            return "Failed to update item. Error: " + e.getMessage();
        }
    }

    // 4. Delete Item
    public String deleteItem(String tableName, String studentId, String stdName) {
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException("Table name is empty or null");
        }

        try {
            Map<String, AttributeValue> key = Map.of(
                "student_id", AttributeValue.builder().s(studentId).build(),
                "std_name", AttributeValue.builder().s(stdName).build()
            );

            DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

            DeleteItemResponse response = dynamoDbClient.deleteItem(deleteItemRequest);
            logger.info("Item deleted successfully: {}", response);
            return "Deleted the item. HTTP status code: " + response.sdkHttpResponse().statusCode();
        } catch (DynamoDbException e) {
            logger.error("Error deleting item from DynamoDB: {}", e.getMessage());
            return "Failed to delete item. Error: " + e.getMessage();
        }
    }
}
