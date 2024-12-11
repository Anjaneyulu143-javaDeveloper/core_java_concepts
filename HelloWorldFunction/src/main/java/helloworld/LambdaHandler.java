package helloworld;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import helloworld.dao.SchoolDao;
import helloworld.utilities.Dynamodb_helper;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;


/**
 * Handler for requests to Lambda function.
 */
public class LambdaHandler implements RequestHandler<Map<String, Object>,Map<String,Object>> {

    private static final  Gson gson = new Gson();

    private final SchoolDao schoolDao;
    private final DynamoDbClient dbClient;

    public LambdaHandler() {
        this.dbClient = Dynamodb_helper.getDynamoDbClient();
        this.schoolDao = new SchoolDao(dbClient);
    }

    @Override
    public Map<String,Object> handleRequest(final Map<String,Object> input, final Context context) {

        Map<String,Object> responseBody = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        int statusCode = 200;

        context.getLogger().log("Request : "+input.toString() + "\n");

        String httpMethod = (String) input.get("httpMethod");
        String path = (String) input.get("path");
        Map<String,Object> body = gson.fromJson((String)input.get("body"),Map.class);
        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");

        responseBody.put("message","Hello world");

        try {

            if(path.endsWith("getUsers") && httpMethod.equalsIgnoreCase("POST")) {

                context.getLogger().log("get users api called \n");

                String tableName = "Users";
                String key = "userId";
                schoolDao.readItem(tableName, key);

            }

        } catch (DynamoDbException e) {
            statusCode = 500;
            responseBody.put("error", "DynamoDB Exception: " + e.getMessage());
            e.printStackTrace();
        }
        response.put("statusCode", statusCode);
        response.put("body", gson.toJson(responseBody));

        return response;
    }
}
