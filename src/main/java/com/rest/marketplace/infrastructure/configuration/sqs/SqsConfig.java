package com.rest.marketplace.infrastructure.configuration.sqs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class SqsConfig {

	@Value("${aws.sqs.endpoint}")
	private String endpoint;

	@Value("${aws.sqs.region}")
	private String region;

	@Value("${aws.access-key}")
	private String accessKey;

	@Value("${aws.secret-key}")
	private String secretKey;

	@Bean
	public SqsClient sqsClient(){
		return SqsClient.builder()
				.endpointOverride(URI.create(endpoint)) // le dice al cliente de AWS que en vez de ir a la nube real, vaya a LocalStack. Cuando se migre a AWS real, simplemente quitás esta línea y el cliente usa el endpoint real automáticamente.
				.region(Region.of(region))
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
				.build();
	}
}
