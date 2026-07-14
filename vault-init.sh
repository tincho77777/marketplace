#!/bin/sh
export VAULT_ADDR='http://127.0.0.1:8200'

# usa variable de entorno o localhost por defecto
DB_HOST=${DB_HOST:-localhost}
REDIS_HOST=${REDIS_HOST:-localhost}
RABBIT_HOST=${RABBIT_HOST:-localhost}
SQS_ENDPOINT=${SQS_ENDPOINT:-http://localhost:4566}
EXCHANGE_RATE_API_KEY=${EXCHANGE_RATE_API_KEY:-TU_API_KEY}

echo "⏳ Esperando que Vault esté listo..."
sleep 3

vault login myroot

echo "📦 Cargando secretos en Vault..."
echo "🔧 DB_HOST: ${DB_HOST}"
echo "🔧 REDIS_HOST: ${REDIS_HOST}"
echo "🔧 RABBIT_HOST: ${RABBIT_HOST}"
echo "🔧 SQS_ENDPOINT: ${SQS_ENDPOINT}"

vault kv put secret/marketplace/database \
  spring.datasource.url="jdbc:postgresql://${DB_HOST}:5432/marketplace" \
  spring.datasource.username='postgres' \
  spring.datasource.password='postgres'

vault kv put secret/marketplace/redis \
  spring.data.redis.host="${REDIS_HOST}" \
  spring.data.redis.port='6379'

vault kv put secret/marketplace/rabbitmq \
  spring.rabbitmq.host="${RABBIT_HOST}" \
  spring.rabbitmq.port='5672' \
  spring.rabbitmq.username='admin' \
  spring.rabbitmq.password='admin'

vault kv put secret/marketplace/aws \
  aws.access-key='test' \
  aws.secret-key='test' \
  aws.sqs.endpoint="${SQS_ENDPOINT}" \
  aws.sqs.region='us-east-1' \
  aws.sqs.queue-name='product-created-queue' \
  aws.sqs.queue-url='http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/product-created-queue'

vault kv put secret/marketplace/external-apis \
  exchange-rate.api-key="${EXCHANGE_RATE_API_KEY}"

echo "✅ Secretos cargados exitosamente en Vault"
echo "🌐 Ambiente: DB=${DB_HOST}, REDIS=${REDIS_HOST}, RABBIT=${RABBIT_HOST}"