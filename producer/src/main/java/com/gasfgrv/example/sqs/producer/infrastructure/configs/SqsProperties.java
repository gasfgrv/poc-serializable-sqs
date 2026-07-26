package com.gasfgrv.example.sqs.producer.infrastructure.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.sqs")
public record SqsProperties(String queueUrl) {
}
