#!/usr/bin/env groovy
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
@Grapes(
    @Grab(group='com.fasterxml.jackson.core', module='jackson-core', version='2.8.8')
)
// https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk
@Grapes(
    @Grab(group='com.amazonaws', module='aws-java-sdk', version='1.11.116')
)
import com.amazonaws.services.ec2.*
import com.amazonaws.services.s3.*
import com.amazonaws.services.cloudformation.*
import com.amazonaws.auth.*
import com.amazonaws.auth.profile.*
import com.amazonaws.regions.*

class AWSClient {
    
    AWSCredentialsProvider credentials
    AmazonS3 s3Client
    AmazonEC2 ec2Client
    AmazonCloudFormation

    public AWSClient(String credentials, String region) {
        AWSCredentialsProvider credentials = new ProfileCredentialsProvider(credentials)


        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().
                       withCredentials(credentials).
                       withRegion(region).
                       build()

        AmazonEC2 ec2Client = AmazonEC2ClientBuilder.standard().
                       withCredentials(credentials).
                       withRegion(region).
                       build()

        AmazonCloudFormation CloudFormationClient = AmazonCloudFormationClientBuilder.standard().
                       withCredentials(credentials).
                       withRegion(region).
                       build()

        this.credentials = credentials
        this.s3Client = s3Client
        this.ec2Client = ec2Client
        this.CloudFormationClient = CloudFormationClient
    }
}

class Stack {
    String name
    String cfm
    Collection params

    public Stack (String name, String cfm, Collection params) {
        this.name = name
        this.cfm = cfm
        this.params = params
    }
}
