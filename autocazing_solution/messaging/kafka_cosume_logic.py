from messaging.kafka_instance import consumer

# 비동기 Kafka 메시지 소비
async def consume_messages():
    await consumer.start()
    try:
        async for message in consumer:
            print("Received:", message.value)
    finally:
        await consumer.stop()