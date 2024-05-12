import asyncio
import json
from messaging.kafka_instance import consumer

# 비동기 Kafka 메시지 소비
async def consume_messages():
    current_loop = asyncio.get_running_loop()
    while True:
        message = await current_loop.run_in_executor(None, consumer.poll, 1.0)
        if not message:
            continue
            
        for tp, messages in message.items():
            for message in messages:
                if message.error:
                    print(f"Kafka Consumer error: {message.error}")
                    continue
                
                try:
                    user_data = json.loads(message.value.decode('utf-8'))
                    print("Received user data:", user_data)
                except json.JSONDecodeError:
                    print("Error decoding message JSON")