package helloworld;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import helloworld.dao.SchoolDao;
import helloworld.utilities.Dynamodb_helper;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Handler for requests to Lambda function.
 */
public class LambdaHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private static final Gson gson = new Gson();

    private final SchoolDao schoolDao;
    private final DynamoDbClient dbClient;

    public LambdaHandler() {
        this.dbClient = Dynamodb_helper.getDynamoDbClient();
        this.schoolDao = new SchoolDao(dbClient);
    }

    @Override
    public Map<String, Object> handleRequest(final Map<String, Object> input, final Context context) {

        Map<String, Object> responseBody = new HashMap<>();

        try {
            // Logging the input request
            context.getLogger().log("Request: " + gson.toJson(input) + "\n");

            // Extracting HTTP method and path
            String httpMethod = (String) input.get("httpMethod");
            String path = (String) input.get("path");
            Map<String, String> queryStringParameters = (Map<String, String>) input.get("queryStringParameters");

            // Default response
            responseBody.put("message", "Hello world");

            // Handle `getUsers` API
            if (path.endsWith("getUsers") && httpMethod.equalsIgnoreCase("GET")) {
                context.getLogger().log("get users API called\n");

                // Validate and fetch `student_id` from query string
                String stdId = queryStringParameters != null ? queryStringParameters.get("student_id") : null;
                if (stdId == null || stdId.isEmpty()) {
                    responseBody.put("error", "Missing or invalid 'student_id' parameter.");
                    return buildResponse(400, responseBody); // Return HTTP 400 for Bad Request
                }

                // Query the database
                String tableName = "students"; // DynamoDB table name
                String data = schoolDao.readItem(tableName, stdId);

                if (data == null || data.isEmpty()) {
                    responseBody.put("message", "No data found for student_id: " + stdId);
                    return buildResponse(404, responseBody); // Return HTTP 404 for Not Found
                }

                // Extract the student_id from the data
                String retrievedStudentId = parseStudentIdFromData(data); // Helper method to extract ID

                // Check for mismatched student IDs
                if (!stdId.equals(retrievedStudentId)) {
                    responseBody.put("error", "Mismatched student_id. Requested: " + stdId + ", but found: " + retrievedStudentId);
                    return buildResponse(400, responseBody); // Return HTTP 400 for Bad Request
                }

                // Sanitize and return the data
                String sanitizedData = data.replace("\n", " ");
                responseBody.put("message", sanitizedData);
                return buildResponse(200, responseBody); // Return HTTP 200 for OK
            }

            // Return default message if no specific path is matched
            return buildResponse(400, responseBody);

        } catch (Exception e) {
            // Log and return error response
            context.getLogger().log("Error: " + e.getMessage());
            responseBody.put("error", "Internal server error occurred.");
            return buildResponse(500, responseBody); // Return HTTP 500 for Internal Server Error
        }
    }

    /**
     * Helper method to build the HTTP response.
     *
     * @param statusCode   HTTP status code
     * @param responseBody Response body as a map
     * @return Complete HTTP response as a map
     */
    private Map<String, Object> buildResponse(int statusCode, Map<String, Object> responseBody) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", statusCode);
        response.put("body", gson.toJson(responseBody));
        response.put("headers", Map.of("Content-Type", "application/json"));
        return response;
    }

    /**
     * Helper method to parse `student_id` from the retrieved data.
     *
     * @param data The retrieved data from the database
     * @return The extracted `student_id` or null if not found
     */
    private String parseStudentIdFromData(String data) {
        // Assuming the data is in the format: "Student(id=101, name=Sowmya, age=23, grade=A)"
        if (data.contains("id=")) {
            int startIndex = data.indexOf("id=") + 3; // Start after "id="
            int endIndex = data.indexOf(",", startIndex); // End at the first comma
            return data.substring(startIndex, endIndex).trim();
        }
        return null; // Return null if the format is invalid
    }
}
