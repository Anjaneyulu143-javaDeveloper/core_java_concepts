package helloworld.dao;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import helloworld.pojo.Student;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class SchoolDao {

    private String tableName = "Student";
    private final DynamoDbClient dynamoDbClient;

    public SchoolDao(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    private static final Logger logger = LoggerFactory.getLogger(SchoolDao.class);

    /*
     * curd operations in dynamoDB
     * 1. create Item in DB
     * 2. Read Item from DB
     * 3. update Item in DB
     * 4. Delete Item from DB
     */

     // 1. Create Item
     public String createItem(String tableName,Student std) {
        if(tableName == null || tableName.isEmpty()) {
            throw new NullPointerException("Table name is empty or null");
        } else {
            this.tableName = tableName;
        }
        try{
            PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(Map.of(
                    "id",AttributeValue.builder().s(std.getId()).build(),
                    "name",AttributeValue.builder().s(std.getName()).build(),
                    "age",AttributeValue.builder().n(String.valueOf(std.getAge())).build(),
                    "grade",AttributeValue.builder().s(std.getGrade()).build()
                )) .build();

            PutItemResponse response = dynamoDbClient.putItem(putItemRequest);
            logger.info("Item created successfully: {}" , response);

             return "Added successfully . Http status code : "+response.sdkHttpResponse().statusCode();

        } catch (DynamoDbException e) {
            logger.error("creating Item in create student has DBException: {}", e.getMessage());
            return "Failed to add item. Error: " + e.getMessage();
        } catch (Exception e) {
            logger.error("Error creating Item in create student : {}", e.getMessage());
            e.printStackTrace();
            return "An unexpected error occured in createItem "+e.getMessage();
        }
      }

      // 2. Read Item
      public String readItem(String tableName , String id) {
        if(tableName == null || tableName.isEmpty()) {
            throw new NullPointerException("Table name is empty or null");
        } else {
            this.tableName = tableName;
        }

        try {
            GetItemRequest getItemRequest = GetItemRequest.builder()
                 .tableName(tableName)
                 .key(Map.of("id",AttributeValue.builder().s(id).build()))
                 .build();

                 GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);
                 if(getItemResponse.hasItem()) {
                    //String data = getItemResponse.item().get("data").s();
                    Map<String, AttributeValue> item = getItemResponse.item();
                    Student student = new Student();
                    student.setId(item.get("id").s());
                    student.setName(item.get("name").s());
                    student.setAge((int) Long.parseLong(item.get("age").n()));
                    student.setGrade(item.get("grade").s());
                    logger.info("Item Found: {}", student.getName());
                    return "Student Data : "+student.getName();
                 } else {
                    logger.error("Item Not Found");
                    return "Error getting data status code :" + getItemResponse.sdkHttpResponse().statusCode();
                 }

        } catch (DynamoDbException e) {
            logger.error("creating Item in create student has DBException: {}", e.getMessage());
            return "Failed to read item. Error: " + e.getMessage();
        } catch(Exception e) {
            logger.error("Error reading item from DynamoDB: {}", e.getMessage());
            e.printStackTrace();
            return "An unexpected error occured in readItem "+e.getMessage();
        }
      }
     // 3. Update Item
      public String updateItem (String tableName, Student student) {

        if(tableName == null || tableName.isEmpty()) {
            throw new NullPointerException("Table name is empty or null");
        } else {
            this.tableName = tableName;
        }
        try {
            Map<String, AttributeValueUpdate> updates = new HashMap<>();
            updates.put("name", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(student.getName()).build())
                    .action(AttributeAction.PUT)
                    .build());
            updates.put("age", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().n(
                            String.valueOf(student.getAge())).build())
                    .action(AttributeAction.PUT)
                    .build());
            updates.put("grade", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(student.getGrade()).build())
                    .action(AttributeAction.PUT)
                    .build());

            UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                     .tableName(tableName)
                     .key(Map.of("id",AttributeValue.builder().s(student.getId()).build()))
                    //  .updateExpression("SET data = :newValue")
                    //  .expressionAttributeValues(Map.of("newValue",AttributeValue.builder().s(newValue).build()))
                     .attributeUpdates(updates)
                     .build();

            UpdateItemResponse updateResponse = dynamoDbClient.updateItem(updateItemRequest);
            logger.info("Item updated successfully: {}", updateResponse.sdkHttpResponse().statusCode());
            return "updated the Item : "+updateResponse.sdkHttpResponse().statusCode();

        } catch (DynamoDbException e) {
            logger.error("Error updating item in DynamoDB: {}", e.getMessage());
            e.printStackTrace();
            return "Failed to update item. Error: " + e.getMessage();
        } catch(Exception e) {
            logger.error("Error updating item from DynamoDB: {}", e.getMessage());
            e.printStackTrace();
            return "An unexpected error occured in readItem "+e.getMessage();
        }

      }

      // 4. Delete Item
      public String deleteItem(String tableName, String id) {

        try {
            DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                  .tableName(tableName)
                  .key(Map.of("id",AttributeValue.builder().s(id).build()))
                  .build();

                  DeleteItemResponse deleteResponse = dynamoDbClient.deleteItem(deleteItemRequest);
                  logger.info("Item is deleted: {}" ,deleteResponse);
                  return "Deleted the Item :"+id;

        } catch (DynamoDbException e) {
            logger.error("Error deleting item from DynamoDB: {}", e.getMessage());
            e.printStackTrace();
            return "Failed to delete item. Error: " + e.getMessage();
        }
      }

}
