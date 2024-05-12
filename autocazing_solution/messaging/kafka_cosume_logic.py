import asyncio
import json
from messaging.kafka_instance import consumer

# 비동기 Kafka 메시지 소비
async def consume_messages():
    current_loop = asyncio.get_running_loop()
    while True:
        message = await current_loop.run_in_executor(None, consumer.poll, 1.0)
        if message is None:
            continue
        if message.error():
            print(f"Kafka Consumer error: {message.error()}")
            continue
        # 예시 데이터 처리 로직
        user_data = json.loads(message.value())
        print("Received user data:", user_data)