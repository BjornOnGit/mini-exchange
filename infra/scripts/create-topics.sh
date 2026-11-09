#!/usr/bin/env bash
# Creates all Kafka topics. Safe to run repeatedly.
set -euo pipefail

COMPOSE_FILE="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)/docker-compose.yml"

kafka_topics() {
  docker compose -f "$COMPOSE_FILE" exec -T kafka \
    /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 "$@"
}

# name:partitions
TOPICS=(
  "orders.commands.BTC-USDT:1"
  "orders.commands.ETH-USDT:1"
  "engine.trades:2"
  "engine.order-events:2"
  "engine.book-updates:2"
  "orders.external-route:2"
  "gateway.external-fills:2"
)

for entry in "${TOPICS[@]}"; do
  name="${entry%%:*}"
  partitions="${entry##*:}"
  kafka_topics --create --if-not-exists --topic "$name" \
    --partitions "$partitions" --replication-factor 1
  kafka_topics --create --if-not-exists --topic "$name.dlt" \
    --partitions 1 --replication-factor 1
done

kafka_topics --list