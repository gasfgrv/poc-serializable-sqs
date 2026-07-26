package com.gasfgrv.example.sqs.consumer.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.sqs")
public record SqsProperties(String queueUrl) {
}
