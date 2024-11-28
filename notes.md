This document contains the most important notes I recorded during the project development.

# Java

## Maven

Maven is a build automation and project management tool that helps developers build, publish, and deploy projects.

### Commands 

`mvn clean package`

- clean: cleans the project (this delets _target_ directory)
- package: builds the project and generates `.jar` file in _target_ directory

## Classes

### UUID

Universally Unique Identifier is a 128-bit value used to uniquely identify objects or entities in a distributed system. Java provides built-in support for UUID thrgouh `java.util.UUID` class, which offers methods to generate and manipulate UUIDs.

## Libraries

### Lombok

Lombok reduces boilerplate code by automatically generating repetitive methods like getters, setters, constructors, and more during compiling time. It uses annotations to simplify code, making classes cleaner and more readable.

### Jackson

Jackson is a library used for processing JSON data. It provides powerful and efficient functionality for parsing, generating, transforming, and mapping JSON data to and from Java objects.

# AWS

**Amazon Web Services** is a cloud computing platform. It provides a range of services and tools for business and developers to build, deploy and manage applications and infrastructure in the cloud.

## Lambda

Is a serverless computing service that allows you to run code without provisioning or managing servers. Lambda automatically scales and executes your code in response to speciffic triggers, such as HTTP requests, changes in S3 buckets, updates in DynamoDB, or events from other AWS services.

In this project, during the process of creating a lambda function, ensure to enable URL function, this will help you to test the application. During the deployment step, navigate to _configuration_ tab and disable URL function to prevent unintended access.

### Code tab

- Code source: upload `.jar` file;
- Runtime settings: define the path for the handler. In this project, the path is **com.rocketseat.Main::handleRequest**.

### Monitor tab

Go to **View CloudWatch logs** to access Log groups and see Log streams. It will help you identify what problems or events occurred in your lambda function.

### Configuration tab

- **Permissions** section: set what services this lambda can access and which operations it can execute. Click on the link below 'Role name'.
    - Add permissions
        - Attach policies: you can use standard policies provided by AWS, such as giving full access to S3 services;
        - Create inline policy: you can create and configure your own policy. For this project, the following permissions are recommended: access to a specific bucket from your S3 that will be used in this application, in my case this bucket is named **url-shortener-storage-g**. Also grant permisson for only executing _PutObject_ and _GetObject_ operations.

- **Function URL** section: here you can create, edit or delete a lambda function URL.

## CloudWatch

Is a fully managed monitoring and observability service. It helps developers and system administrators track the performance and health of AWS resources, applications, and services. CloudWatch collects and visualizes metrics, logs, and events in real time, enabling users to identify and address performance issues, optimize resources, and ensure smooth operations.

## S3

**Simple Storage Service** is a highly scalable, durable, and secure object storage service in the cloud. It allows users to store, retrieve, and manage data as objects in buckets, which act as cotainters for storage.

Create your bucket (storage unit) here.

## API Gateway

Is a fully managed service that allows developers to create, publish, and manage APIs at any scale. It acts like a gateaway for applications to securely connect to back-end services, such as AWS Lambda, EC2, or any external HTTP endpoint.

Create your API. The API type for this project is HTTP API.

 - **Develop** section:
    - Route: create a route for **POST** method in **/create** path. Then select _POST_ and attach an integration with **URLShortener** lambda function.
    - Route: create a route for **GET** methos in _/{urlCode}_ (urlCode is between curly braces, meaning it represents the parameter). Then attach a integration with **RedirectURLShortener** lambda function to this route.

(Note that $default Stage has auto-deploy enabled, if you create a new stage, the new one may need you to manually deploy. Remember that you can change the configuration for each stage).


# Testing

There are several ways to make HTTP requests for testing your application. Here are some examples:

## Terminal

The following command line represents how to make HTTP request from terminal.

Request example: ` curl -X POST https://aws-lambda-example.aws/ -H "Content-Type: application/json" -d '{"expirationTime":"123456789", "originalURL":"https://github.com"}'`

Response example: `{"code":"abc123de45"}`

## [Postman](https://www.postman.com/downloads/)

Is a platform and API development tool used to design, test and document APIs. Postman provides an intuitive interface to send requests, view responses, and debug issues.

## Thunder Client (extension for VS Code)

Thunder Client is a lightweight Rest API Client Extension for VS Code, hand-crafted by Ranga Vadhineni with a focus on simplicity, clean design and local storage.