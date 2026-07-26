PHONY: build-containers destroy-containers check-messages check-queue

QUEUE_URL = http://sqs.sa-east-1.localhost.localstack.cloud:4566/000000000000/pedidos

build-containers:
	docker compose -f docker/compose.yml up -d

destroy-containers:
	docker compose -f docker/compose.yml down -v

check-messages: build-containers
	docker exec -it aws-local awslocal sqs receive-message --queue-url $(QUEUE_URL)

check-queue: build-containers
	docker exec -it aws-local awslocal sqs get-queue-attributes --queue-url $(QUEUE_URL) --attribute-names All