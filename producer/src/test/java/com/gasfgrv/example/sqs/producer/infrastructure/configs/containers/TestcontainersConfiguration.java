package com.gasfgrv.example.sqs.producer.infrastructure.configs.containers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @SuppressWarnings("resource")
    public LocalStackContainer localStackContainer() {
        return new LocalStackContainer(DockerImageName.parse("localstack/localstack:4.14.0"))
                .withExposedPorts(4566)
                .withServices("sqs");
    }

    @Bean
    public DynamicPropertyRegistrar awsPropertyRegistrar(LocalStackContainer container) {
        var endpoint = container.getEndpoint().toString();
        return registry -> {
            registry.add("spring.cloud.aws.region.static", container::getRegion);
            registry.add("spring.cloud.aws.credentials.access-key", container::getAccessKey);
            registry.add("spring.cloud.aws.credentials.secret-key", container::getSecretKey);
            registry.add("spring.cloud.aws.sqs.endpoint", () -> endpoint);
        };
    }

    @Bean
    public CommandLineRunner commandLineRunner(LocalStackContainer container) {
        return args ->
                container.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", "pedidos");
    }

}
