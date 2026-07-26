#!/bin/sh

awslocal sqs create-queue \
    --queue-name pedidos \
    --attributes '{
        "DelaySeconds":"10",
        "MaximumMessageSize": "262144",
        "MessageRetentionPeriod": "12099600",
        "VisibilityTimeout": "30",
        "ReceiveMessageWaitTimeSeconds": "20"
    }'
