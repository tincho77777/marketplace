#!/bin/sh
export VAULT_ADDR='http://127.0.0.1:8200'

echo "Esperando que Vault esté listo..."
sleep 2

vault login myroot

vault kv put secret/marketplace/database \
  spring.datasource.url='jdbc:postgresql://localhost:5432/marketplace' \
  spring.datasource.username='postgres' \
  spring.datasource.password='postgres'

vault kv put secret/marketplace/redis \
  spring.data.redis.host='localhost' \
  spring.data.redis.port='6379'

vault kv put secret/marketplace/rabbitmq \
  spring.rabbitmq.host='localhost' \
  spring.rabbitmq.port='5672' \
  spring.rabbitmq.username='admin' \
  spring.rabbitmq.password='admin'

vault kv put secret/marketplace/aws \
  aws.access-key='test' \
  aws.secret-key='test' \
  aws.sqs.endpoint='http://localhost:4566' \
  aws.sqs.region='us-east-1' \
  aws.sqs.queue-name='product-created-queue' \
  aws.sqs.queue-url='http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/product-created-queue'

vault kv put secret/marketplace/external-apis \
  exchange-rate.api-key='848e46f7b20b6bdf9d887b8a'

echo "✅ Secretos cargados en Vault"