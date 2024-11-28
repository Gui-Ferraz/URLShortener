package com.rocketseat;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/*
 * Main class implementing the {@link com.amazonaws.services.lambda.runtime.RequestHandler} interface.
 * 
 * <p>This class handles HTTP requests triggered by an AWS Lambda function. It processes input parameters
 * from an HTTP POST request and generates an 8-character UUID as the response.</p>
 * 
 * @author Guilherme M. Ferraz
 * @see com.amazonaws.services.lambda.runtime.RequestHandler
 */
public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> {     // RequestHandler< input, output>

    private final ObjectMapper objectMapper = new ObjectMapper();   // Handles JSON data
    private final S3Client s3Client = S3Client.builder().build();

    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {  // input handleRequest(output, context)
        // 'body' gets HTML body value as String
        String body = input.get("body").toString();

        Map<String, String> bodyMap;
        try {
            // 'bodyMap' gets HTML body value as Map<String, String> (e.g. "originalURL" : "https://amazon.com")
            bodyMap = objectMapper.readValue(body, Map.class);
        } catch (Exception exception) {
            throw new RuntimeException("Error parsing json body:" + exception.getMessage(), exception);
        }

        String originalURL =  bodyMap.get("originalURL");
        String expirationTime = bodyMap.get("expirationTime");
        long expirationTimeInSeconds = Long.parseLong(expirationTime);

        // Generates a UUID and stores the firsts 8 numbers in 'shortURLCode'
        String shortURLCode = UUID.randomUUID().toString().substring(0, 8);

        URLData urlData = new URLData(originalURL, expirationTimeInSeconds);

        try {
            // Writes 'urlData' content in JSON's format to 'urlDataJson'
            String urlDataJson = objectMapper.writeValueAsString(urlData);

            // putObject is a S3 operation to insert objects into a Bucket
            PutObjectRequest request = PutObjectRequest.builder().bucket("url-shortener-storage-g").key(shortURLCode + ".json").build();
            s3Client.putObject(request, RequestBody.fromString(urlDataJson));

        } catch(Exception exception) {
            throw new RuntimeException("Error saving data to S3: " + exception.getMessage(), exception);
        }

        Map<String, String> response = new HashMap<>();
        response.put("code", shortURLCode);

        return response;
    }
}

