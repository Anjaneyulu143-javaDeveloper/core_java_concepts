package helloworld.utilities;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class Dynamodb_helper {

    private static final Logger logger = LoggerFactory.getLogger(Dynamodb_helper.class);

    public static DynamoDbClient getDynamoDbClient() {
        try {
            return DynamoDbClient.builder()
            .endpointOverride(URI.create("http://host.docker.internal:8000"))
             .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("ASIA46ZDFQOUKY5UL2UB", "a89oRKr/hIhFZK9qHNVr4nIQH1z2CKIkuYfcCMV9"))) // Dummy credentials
            .region(Region.US_EAST_1)
            .build();

        } catch (SdkException e) {
            logger.error("Error creating item in DynamoDB: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

}
