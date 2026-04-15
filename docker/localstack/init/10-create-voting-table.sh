#!/usr/bin/env bash
set -euo pipefail

TABLE_NAME="voting-system"

if awslocal dynamodb describe-table --table-name "${TABLE_NAME}" >/dev/null 2>&1; then
  echo "DynamoDB table ${TABLE_NAME} already exists"
  exit 0
fi

awslocal dynamodb create-table \
  --table-name "${TABLE_NAME}" \
  --attribute-definitions \
    AttributeName=pk,AttributeType=S \
    AttributeName=sk,AttributeType=S \
    AttributeName=gsi1pk,AttributeType=S \
    AttributeName=gsi1sk,AttributeType=S \
  --key-schema \
    AttributeName=pk,KeyType=HASH \
    AttributeName=sk,KeyType=RANGE \
  --global-secondary-indexes \
    '[{"IndexName":"gsi1","KeySchema":[{"AttributeName":"gsi1pk","KeyType":"HASH"},{"AttributeName":"gsi1sk","KeyType":"RANGE"}],"Projection":{"ProjectionType":"ALL"}}]' \
  --billing-mode PAY_PER_REQUEST

echo "DynamoDB table ${TABLE_NAME} created"
